/*
 * FileName: RandomUtilsTest.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-11
 */

package cn.eppdev.utils;

import cn.eppdev.utils.random.RandomUtils;
import org.junit.Test;

/**
 * @author fan.hao
 */
public class RandomUtilsTest {
    @Test
    public void test(){
        for(int i=0; i<= 10; i++) {
            System.out.println(RandomUtils.getRandomStr(8, 1));
            System.out.println(RandomUtils.getRandomStr(8, 2));
            System.out.println(RandomUtils.getRandomStr(8, 3));
            System.out.println(RandomUtils.getRandomStr(8, 4));
        }
    }
}
