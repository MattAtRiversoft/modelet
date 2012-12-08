package test;


public class TestReplace {

  public static void main(String[] args) {
    
    String s = "abc'def''fgrg'''rerg'erger";
    System.out.println(s.replaceAll("'{1,1}", "''"));
  }
}
