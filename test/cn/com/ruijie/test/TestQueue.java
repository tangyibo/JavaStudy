/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.ruijie.test;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Administrator
 */
public class TestQueue {
    public static void main(String[] args)
    {
        Queue<String> queue = new LinkedList<>();

        queue.add("a");
        queue.add("b");
        queue.add("c");
        queue.add("d");
        queue.add("e");
        queue.offer("f");
        
        System.out.println("size:" + queue.size());
        System.out.println("poll:" + queue.poll());

        while (!queue.isEmpty()) {
            System.out.println("pop:" + queue.peek());
            queue.remove();
        }

         
        
    }
}
