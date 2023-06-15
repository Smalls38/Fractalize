package com.god.fractal;

public class Stack<T> {
    int size = 0; //keeps track of the size
    StackNode<T> StackNode; // current StackNode
    public void push(T data) {
        size++; //increase size
        StackNode<T> temp = new StackNode<>(data); //make new StackNode
        temp.setNext(StackNode); //link the previous one to this StackNode
        StackNode = temp; //set new StackNode as new StackNode
    }
    public T pop(){
        if (size == 0){ //can't pop something that doesn't exist
            throw new RuntimeException("stack is empty my guy");
        }
        size--; //decrease size
        T temp = StackNode.getData(); //get the data of the StackNode that is about to be popped
        StackNode = StackNode.getNext();
        return temp;
    }
    public T peak(){
        if (size == 0){
            throw new RuntimeException("stack is empty my guy");
        }
        return StackNode.getData();
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public boolean isFull(){
        return false; //im confused, would the stack ever be full??
    }
}

class StackNode<T> {
    private T data; //the data
    private StackNode<T> next; //the next StackNode

    StackNode(T d) {
        data = d;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public StackNode<T> getNext() {
        return next;
    }

    public void setNext(StackNode<T> StackNode) {
        this.next = StackNode;
    }
}