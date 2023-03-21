package com.stm.controller;

import java.util.HashSet;

public class SuanfaTest {
    public static void main(String[] args) {
        /*int mid = 5/ 2 ;
        System.out.println(mid);
        Math.min(2, 4);
        System.out.println(Math.min(4, 2));*/
        
        int [] nums = {1,7,2,15};
        //nums = rotate(nums,3);
        //nums = twoSum(nums,9);
       // System.out.println(nums[0]+"==:=="+nums[1]);

        //System.out.println( pushDominoes1("...L"));
        String  s ="a-bC-dEf-ghIj";
        
        //numberOfGoodSubsets(nums);

       // String test = reverseOnlyLetters(s);
       // System.out.println( test);

        //char [] chars = {'b','c','d','e'};
       // String test = reverseString(chars);
       // System.out.println( test);
        
        //String str = "Let's take LeetCode contest";
       // reverseWords(str);
        //String str = complexNumberMultiply("1+1i","1+1i");
        //System.out.println( str);
       // int[][] xuanlou = {{0,1},{1,0},{0,1},{1,2},{2,0},{3,4}};
       //new SuanfaTest().maximumRequests(5,xuanlou);

        //System.out.println( convert("ABC",1));
       
        System.out.println(  addDigits(299));
    }

    public static int[] rotate(int[] nums, int k) {
        /*int count =0;
        int [] newNums=new int[nums.length];
        while(k>count){
            for(int i = 0;i<nums.length;i++){
                if(i==0){
                    newNums[i] = nums[nums.length-1];
                }else{
                    newNums[i] = nums[i-1];
                }
            }
            nums=newNums;
            count++;
        }*/
        int n = nums.length;
        int res[] = new int[n];
        for(int i = 0;i < n;i++){
            res[(i + k) % n] = nums[i];
        }
        return res;
    }

    public static int[] twoSum(int[] numbers, int target) {
        int n = numbers.length;
        int left = 0;
        int right = n-1;
        int[] newNumbers = new int[2];
        while(right>0){
            if(numbers[right]+numbers[left]==target){
                newNumbers[0]=left+1;
                newNumbers[1]=right+1;
                return newNumbers;
            }
            if(right-1==left){
                right --;
                left=0;
            }else{
                left++;
            }
        }
        return newNumbers;
    }

    public static String pushDominoes(String dominoes) {
        dominoes = "L" + dominoes + "R";
        int l = 0;
        StringBuilder res = new StringBuilder();
        for (int r = 1; r < dominoes.length(); ++r) {
            if (dominoes.charAt(r) == '.') {
                continue;
            }
            if (l != 0) { // 虚拟的牌不放入结果
                res.append(dominoes.charAt(l));
            }
            int mid = r - l - 1;
            if (dominoes.charAt(l) == dominoes.charAt(r)) {
                for (int i = 0; i < mid; ++i) {
                    res.append(dominoes.charAt(l));
                }
            } else if (dominoes.charAt(l) == 'L' && dominoes.charAt(r) == 'R') {
                for (int i = 0; i < mid; ++i) {
                    res.append('.');
                }
            } else {
                for (int i = 0; i < mid / 2; ++i) {
                    res.append('R');
                }
                if (mid % 2 == 1) {
                    res.append('.');
                }
                for (int i = 0; i < mid / 2; ++i) {
                    res.append('L');
                }
            }
            l = r;
        }
        return res.toString();

    }

   /* n 张多米诺骨牌排成一行，将每张多米诺骨牌垂直竖立。在开始时，同时把一些多米诺骨牌向左或向右推。
    每过一秒，倒向左边的多米诺骨牌会推动其左侧相邻的多米诺骨牌。同样地，倒向右边的多米诺骨牌也会推动竖立在其右侧的相邻多米诺骨牌。
    如果一张垂直竖立的多米诺骨牌的两侧同时有多米诺骨牌倒下时，由于受力平衡， 该骨牌仍然保持不变。
    就这个问题而言，我们会认为一张正在倒下的多米诺骨牌不会对其它正在倒下或已经倒下的多米诺骨牌施加额外的力。
    给你一个字符串 dominoes 表示这一行多米诺骨牌的初始状态，其中：
    dominoes[i] = 'L'，表示第 i 张多米诺骨牌被推向左侧，
    dominoes[i] = 'R'，表示第 i 张多米诺骨牌被推向右侧，
    dominoes[i] = '.'，表示没有推动第 i 张多米诺骨牌。
    返回表示最终状态的字符串。
    示例 1：
    输入：dominoes = "RR.L"
    输出："RR.L"
    解释：第一张多米诺骨牌没有给第二张施加额外的力。
    
    示例 2：
    输入：dominoes = ".L.R...LR..L.."
    输出："LL.RR.LLRRLL.."
    */
    public static String pushDominoes1(String dominoes) {
        dominoes = "L"+dominoes+"R";
        int l = 0;
        StringBuffer str = new StringBuffer();
        for (int r=1;r<dominoes.length();++r){
            
            //如果为站立的牌就直接跳过处理
            if(dominoes.charAt(r) == '.'){
                continue ;
            }
            
            if(l!=0){
                str.append(dominoes.charAt(l));
            }
            
            int mid = r-l-1;
            //如果左右两端的倾倒方向一致则站立的牌直接向相同方向倾倒
            if(dominoes.charAt(l)==dominoes.charAt(r)){
                for(int i =0 ;i<mid;++i){
                    str.append(dominoes.charAt(r));
                }//如果左右两端的倾倒方向相反则站立的牌还是站立
            }else if(dominoes.charAt(l)=='L' && dominoes.charAt(r)=='R'){
                for(int i =0 ;i<mid;++i){
                    str.append(".");
                }
                //如果左端向右倾倒,右边向左倾倒则需要进行判断
            }else{
                //向右倒的应该有一半的牌
                for(int i=0;i<mid/2;++i){ 
                    str.append("R");
                }
                //最中间单独的牌应该是站立的
                if(mid%2==1){
                    str.append(".");
                }
                //向左倒的应该有一半的牌
                for(int i=0;i<mid/2;++i){
                    str.append("L");
                }
            }

            l = r;
        }
        
        
        return str.toString();
    }


    public static int numberOfGoodSubsets(int[] nums) {
        int num=0;
        HashSet<Integer> set = new HashSet<Integer>();
        while(num<nums.length){
            for (int i =0;i<nums.length;i++){
                //如果循环到了同一个值就跳过
                if(num==i){
                    continue;
                }
                //System.err.println( nums[num]*nums[i]);
                set.add(nums[num]*nums[i]);
            }
            
            num ++;
        }
        
        return 0;
    }

    /*给你一个字符串 s ，根据下述规则反转字符串：

    所有非英文字母保留在原有位置。
    所有英文字母（小写或大写）位置反转。

    返回反转后的 s 。*/
    public static String reverseOnlyLetters(String s) {

        char [] str = s.toCharArray();
        int left = 0;
        int right = str.length;
        
        char [] newStr = new char[str.length];
        while(left<right){
            char c = str[left];
            int rIndex = right-1;
            char rc = str[rIndex];
            //判断是否为字母
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) &&
                    ((rc >= 'a' && rc <= 'z') || (rc >= 'A' && rc <= 'Z'))) {
                char temp = str[left];
                newStr[left] = str[rIndex];
                newStr[rIndex] = temp;
                left ++;
                right --;
            }else if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) &&
                    ((rc >= 'a' && rc <= 'z') || (rc >= 'A' && rc <= 'Z'))){
                newStr[left] = str[left];
                left ++;
            }else if(((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) &&
                    !((rc >= 'a' && rc <= 'z') || (rc >= 'A' && rc <= 'Z'))){
                newStr[rIndex] = str[rIndex];
                right --;
            }else{
                newStr[left] = str[left];
                left ++;
            }
        }
        
        return String.valueOf(newStr);
    }

    public static String reverseString(char[] s) {
        int left = 0;
        int right = s.length;
        char[] newStr = new char[s.length];

        while (left < right) {
            int rIndex = right - 1;
            char temp = s[left];
            newStr[left] = s[rIndex];
            newStr[rIndex] = temp;
            left++;
            right--;

        }

        return String.valueOf(newStr);
    }

    public static String reverseWords(String s) {
        String [] strs = s.split(" ");
        StringBuffer buffer = new StringBuffer();
        for(String str :strs){
            char[] chars = str.toCharArray();
            int left = 0;
            int right = chars.length;

            while (left < right) {
                int rIndex = right - 1;
                char temp = chars[left];
                chars[left] = chars[rIndex];
                chars[rIndex] = temp;
                left++;
                right--;
            }
            buffer.append(String.valueOf(chars)+" ");
        }
        buffer.toString().format("");
        return buffer.toString().trim();
    }

    /*
    复数 可以用字符串表示，遵循 "实部+虚部i" 的形式，并满足下述条件：
    实部 是一个整数，取值范围是 [-100, 100]
    虚部 也是一个整数，取值范围是 [-100, 100]
    i2 == -1
    给你两个字符串表示的复数 num1 和 num2 ，请你遵循复数表示形式，返回表示它们乘积的字符串。
     */

    //用 real1 和 imag1​ 分别表示 num1的实部和虚部，用 real2 和 imag2分别表示  的实部和虚部，则两个复数的乘法计算如下：
    //(real1​×real2​−imag1​×imag2​)+(real1​×imag2​+imag1​×real2​)×i​
    public static String complexNumberMultiply(String num1, String num2) {
        System.err.println( num1.charAt(3));
        String[] complex1 = num1.split("\\+|i");
        String[] complex2 = num2.split("\\+|i");
        int real1 = Integer.parseInt(complex1[0]);
        int imag1 = Integer.parseInt(complex1[1]);
        int real2 = Integer.parseInt(complex2[0]);
        int imag2 = Integer.parseInt(complex2[1]);
        return String.format("%d+%di", real1 * real2 - imag1 * imag2, real1 * imag2 + imag1 * real2);
    }


    int[] delta;
    int ans = 0, cnt = 0, zero, n;

    public int maximumRequests(int n, int[][] requests) {
        delta = new int[n];
        zero = n;
        this.n = n;
        dfs(requests, 0);
        return ans;
    }

    public void dfs(int[][] requests, int pos) {
        if (pos == requests.length) {
            if (zero == n) {
                ans = Math.max(ans, cnt);
            }
            return;
        }

        // 不选 requests[pos]
        dfs(requests, pos + 1);

        // 选 requests[pos]
        int z = zero;
        ++cnt;
        int[] r = requests[pos];
        int x = r[0], y = r[1];
        zero -= delta[x] == 0 ? 1 : 0;
        --delta[x];
        zero += delta[x] == 0 ? 1 : 0;
        zero -= delta[y] == 0 ? 1 : 0;
        ++delta[y];
        zero += delta[y] == 0 ? 1 : 0;
        dfs(requests, pos + 1);
        --delta[y];
        ++delta[x];
        --cnt;
        zero = z;
    }

    public static String convert(String s, int numRows) {
        if(numRows==1){
            return s;
        }
        char[] chars = s.toCharArray();
        String[] newStrs = new String[s.length()>numRows?numRows:s.length()];
        int n = 0;
        int col =0;
        int row = numRows-2;
        while(n<chars.length){
            if(col%numRows==0 && n!=0){
                if(row!=0){
                    newStrs[row]=newStrs[row]+String.valueOf(chars[n]);
                    row--;
                }else{
                    col =0;
                    newStrs[col]=newStrs[col]+String.valueOf(chars[n]);
                    col++;
                    row=numRows-2;
                }
            }else{
                if(n<numRows){
                    newStrs[col]=String.valueOf(chars[n]);
                }else{
                    newStrs[col]=newStrs[col]+String.valueOf(chars[n]);
                }
                col++;
            }
            n++;
        }
        StringBuffer re =new StringBuffer();
        for(String newStr:newStrs){
            System.err.println(newStr);
            re =re.append(newStr);
        }
        return re.toString();
    }

    public static int addDigits(int num) {
        if(num<10){
            return num;
        }
        int newNum=0;
        String s = String.valueOf(num);
        char [] chars = s.toCharArray();
        for (char c:chars) {
            newNum +=Integer.valueOf(String.valueOf(c));
        }
        return addDigits(newNum);
    }

}
