package com.cyy.filemanager.file.dir;

/**
 * Created by study on 16/12/28.
 * 文件栈
 * 单利
 *
 * 押入一个目录
 * 弹出一个目录
 */

public class DirectoryStack<T> {

//    private static DirectoryStack stack ;
    public DirectoryStack(){}

    private Node first;///第一个

//    public static DirectoryStack instance(){
//        if (stack==null){
//            synchronized (DirectoryStack.class){
//                if (stack == null){
//                    stack = new DirectoryStack();
//                }
//            }
//        }
//        return stack;
//    }


    class Node{
         Node next;
         T object;
    }

    public void push(T t){
        Node node = new Node();
        node.object = t;
        node.next = first;
        first = node;
    }

    ///第一个弹出
    public T pop(){
        Node temp = first;
        first = temp.next;
        return temp.object;
    }

    public T getFirst(){
        return first.object;
    }

}
