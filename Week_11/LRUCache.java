package thinkinjava.leetcode;

import java.util.HashMap;

/**
 *        ----             ----                  ----
 * head  |   |   --->     |   | ele     --->    |   |   tail
 *      |   |    <---    |   |         <---    |   |
 *      ----             ----                  ----
 *  LRU Least Recent Used 它的功能，
 *
 *
 */


public class LRUCache {

    private HashMap<Integer, DLinkNode> cache = new HashMap<>();
    private int capacity;
    private int size;
    // head指向的post永远是最近使用的，tail指向的pre永远是最久未使用的
    private DLinkNode head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;

        this.head = new DLinkNode();
        this.head.pre = null;
        this.tail = new DLinkNode();
        this.tail.post = null;

        head.post = this.tail;
        tail.pre = this.head;
    }

    @Override
    public String toString() {
        return "LRUCache{" +
                "cache=" + cache +
                '}';
    }

    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // 缓存是 {1=1}
        System.out.println(lRUCache.toString());
        lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
        System.out.println(lRUCache.toString());
        System.out.println(lRUCache.get(1));    // 返回 1
        lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        System.out.println(lRUCache.toString());
        System.out.println(lRUCache.get(2));    // 返回 -1 (未找到)
        lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        System.out.println(lRUCache.toString());
        System.out.println(lRUCache.get(1));    // 返回 -1 (未找到)
        System.out.println(lRUCache.get(3));    // 返回 3
        System.out.println(lRUCache.get(4));    // 返回 4

    }

    public int get(int key) {
        DLinkNode node = cache.get(key);

        if (null == node) {
            return -1;
        }
        moveToHead(node);
        return node.value;
    }


    public void put(int key, int value) {
        DLinkNode currNode = this.cache.get(key);
        // 查找key是否存在，存在移到队首，不存在则创建并插入队首
        if (null == currNode) {
            currNode = new DLinkNode();
            currNode.key = key;
            currNode.value = value;

            addNode(currNode);
            this.cache.put(key, currNode);

            ++size;

            // 判断当前容量是否full，not full，则加入
            if (size > capacity) {
                DLinkNode tail = popTail();
                this.cache.remove(tail.key);
                size--;
            }
        } else {
            currNode.value = value;
            moveToHead(currNode);
        }
    }

    class DLinkNode {
        int key;
        int value;
        DLinkNode pre;
        DLinkNode post;

        @Override
        public String toString() {
            return "DLinkNode{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    private void addNode(DLinkNode node) {
        node.post = this.head.post;
        node.pre = this.head;

        this.head.post.pre = node;
        this.head.post = node;
    }

    private void removeNode(DLinkNode node) {
        DLinkNode pre = node.pre;
        DLinkNode post = node.post;

        pre.post = post;
        post.pre = pre;

//        node.pre = null;
//        node.post = null;
    }

    /**
     * 将使用已存在的node移至队首
     *
     * @param node 存在的节点
     */
    private void moveToHead(DLinkNode node) {
        removeNode(node);
        addNode(node);
    }

    private DLinkNode popTail() {
        DLinkNode node = tail.pre;
        removeNode(node);
        return node;
    }
}
