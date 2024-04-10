package method.qr.kiarelemb.utils;

import method.qr.kiarelemb.utils.data.QRStringSearchResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Kiarelemb QR
 * @date 2021/11/27 14:44
 * @apiNote 一个AC自动机
 */
public class QRACSearch {
    private TreeNode root;

    public QRACSearch(String[] keywords) {
        buildTree(keywords);
        addFailure();
    }

    public QRACSearch(List<String> keywords) {
        buildTree(keywords);
        addFailure();
    }

    public ArrayList<QRStringSearchResult> findAll(String text) {
        //可以找到 转移到下个节点 不能找到在失败指针节点中查找直到为root节点
        ArrayList<QRStringSearchResult> results = new ArrayList<>() {
            @Override
            public String toString() {
                return this.stream().map(result -> result.toString() + QRStringUtils.A_WHITE_SPACE_CHAR).collect(Collectors.joining()).trim();
            }
        };
        int index = 0;
        TreeNode mid = root;
        while (index < text.length()) {
            TreeNode temp = null;
            while (temp == null) {
                temp = mid.getSonNode(text.charAt(index));
                if (mid == root) {
                    break;
                }
                if (temp == null) {
                    mid = mid.failure;
                }
            }
            //mid为root 再次进入循环 不需要处理  或者 temp不为空找到节点 节点位移
            if (temp != null) {
                mid = temp;
            }
            for (String result : mid.getResults()) {
                results.add(new QRStringSearchResult(index - result.length() + 1, result));
            }
            index++;
        }
        return results;
    }

    private void buildTree(List<String> keywords) {
        root = new TreeNode(null, QRStringUtils.A_WHITE_SPACE_CHAR);
        //判断节点是否存在 存在转移 不存在添加
        for (String word : keywords) {
            TreeNode temp = root;
            for (char ch : word.toCharArray()) {
                if (temp.containNode(ch)) {
                    temp = temp.getSonNode(ch);
                } else {
                    TreeNode newNode = new TreeNode(temp, ch);
                    temp.addSonNode(newNode);
                    temp = newNode;
                }
            }
            temp.addResult(word);
        }
    }

    private void buildTree(String[] keywords) {
        root = new TreeNode(null, QRStringUtils.A_WHITE_SPACE_CHAR);
        //判断节点是否存在 存在转移 不存在添加
        for (String word : keywords) {
            TreeNode temp = root;
            for (char ch : word.toCharArray()) {
                if (temp.containNode(ch)) {
                    temp = temp.getSonNode(ch);
                } else {
                    TreeNode newNode = new TreeNode(temp, ch);
                    temp.addSonNode(newNode);
                    temp = newNode;
                }
            }
            temp.addResult(word);
        }
    }

    private void addFailure() {
        //设置二层失败指针为根节点 收集三层节点
        //DFA遍历所有节点 设置失败节点 原则: 节点的失败指针在父节点的失败指针的子节点中查找 最大后缀匹配
        ArrayList<TreeNode> mid = new ArrayList<>();//过程容器
        for (TreeNode node : root.getSonsNode()) {
            node.failure = root;
            Collections.addAll(mid, node.getSonsNode());
        }

        //广度遍历所有节点设置失败指针 1.存在失败指针 2.不存在到root结束
        while (mid.size() > 0) {
            ArrayList<TreeNode> temp = new ArrayList<>();//子节点收集器

            for (TreeNode node : mid) {
                TreeNode r = node.getParent().failure;
                while (r != null && !r.containNode(node.getChar())) {
                    r = r.failure;//没有找到,保证最大后缀 (最后一个节点字符相同)
                }
                //是根结
                if (r == null) {
                    node.failure = root;
                } else {
                    node.failure = r.getSonNode(node.getChar());
                    //重叠后缀的包含
                    for (String result : node.failure.getResults()) {
                        node.addResult(result);
                    }
                }
                //收集子节点
                Collections.addAll(temp, node.getSonsNode());
            }
            mid = temp;
        }
        root.failure = root;
    }

    static class TreeNode {

        private final TreeNode parent;
        private final char ch;
        private final ArrayList<String> results;
        private final Hashtable<Character, TreeNode> sonsHash;
        private TreeNode failure;
        private TreeNode[] sonsNode;

        public TreeNode(TreeNode parent, char ch) {

            this.parent = parent;
            this.ch = ch;
            results = new ArrayList<>();
            sonsHash = new Hashtable<>();
            sonsNode = new TreeNode[]{};
        }

        //添加子节点
        public void addSonNode(TreeNode node) {
            sonsHash.put(node.ch, node);
            sonsNode = new TreeNode[sonsHash.size()];
            Iterator<TreeNode> iterator = sonsHash.values().iterator();
            IntStream.range(0, sonsNode.length).filter(i -> iterator.hasNext()).forEach(i -> sonsNode[i] = iterator.next());
        }

        //获取子节点中指定字符节点
        public TreeNode getSonNode(char ch) {
            return sonsHash.get(ch);
        }

        //判断子节点中是否存在该字符
        public boolean containNode(char ch) {
            return getSonNode(ch) != null;
        }

        //添加一个结果到结果字符中
        public void addResult(String result) {
            if (!results.contains(result)) {
                results.add(result);
            }
        }

        //获取字符
        public char getChar() {
            return ch;
        }

        //获取父节点
        public TreeNode getParent() {
            return parent;
        }

        //设置失败指针并且返回
        public TreeNode setFailure(TreeNode failure) {
            this.failure = failure;
            return this.failure;
        }

        //获取所有的孩子节点
        public TreeNode[] getSonsNode() {
            return sonsNode;
        }

        //获取搜索的字符串
        public ArrayList<String> getResults() {
            return results;
        }
    }
}
