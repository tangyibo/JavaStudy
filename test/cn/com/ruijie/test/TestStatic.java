package cn.com.ruijie.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class TestStatic {
    static public int a=10;
    
    static {
        a=20;
    }
    
    public static void main(String[] args)
    {
        System.out.println("value="+TestStatic.a);
    }
}
