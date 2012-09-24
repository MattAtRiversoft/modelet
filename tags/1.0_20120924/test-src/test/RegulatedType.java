package test;


public interface RegulatedType {

  String getValue();
  <T> T newInstanceByBalue(String value);
}
