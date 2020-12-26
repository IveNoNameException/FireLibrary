package it.fireentity.library.command.nodes;

import it.fireentity.library.interfaces.Cacheable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

public class Node<T, K> implements Cacheable<K> {
    private final K key;
    private final T content;
    private final Node<T, K> superNode;
    private final HashMap<K, Node<T, K>> nodes = new HashMap<>();

    public Node(K key, Node<T, K> superNode) {
        this(key, superNode, null);
    }

    public Node(K key, Node<T, K> superNode, T content) {
        this.key = key;
        this.superNode = superNode;
        this.content = content;
        if(superNode!=null) {
            superNode.addSubNode(this);
        }
    }

    public Optional<Node<T, K>> getSubNode(K t) {
        return Optional.ofNullable(nodes.get(t));
    }

    public void addSubNode(Node<T, K> node) {
        nodes.put(node.getKey(), node);
    }

    public Collection<Node<T,K>> getSubNodes() {
        return nodes.values();
    }

    @Override
    public K getKey() {
        return key;
    }

    public Optional<T> getContent() {
        return Optional.ofNullable(content);
    }

    public Optional<Node<T, K>> getSuperNode() {
        return Optional.ofNullable(superNode);
    }

    public LinkedHashMap<K, Node<T, K>> getPath() {
        if(superNode == null) {
            LinkedHashMap<K, Node<T, K>> hashMap = new LinkedHashMap<>();
            hashMap.put(key, this);
            return hashMap;
        }

        LinkedHashMap<K, Node<T, K>> hashMap = superNode.getPath();
        hashMap.put(key, this);
        return hashMap;
    }
}
