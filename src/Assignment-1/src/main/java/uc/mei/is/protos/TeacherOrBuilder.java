// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package uc.mei.is.protos;

public interface TeacherOrBuilder extends
    // @@protoc_insertion_point(interface_extends:model.Teacher)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required int32 id = 1;</code>
   * @return Whether the id field is set.
   */
  boolean hasId();
  /**
   * <code>required int32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>required string name = 2;</code>
   * @return Whether the name field is set.
   */
  boolean hasName();
  /**
   * <code>required string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>required string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>required int32 phoneNumber = 3;</code>
   * @return Whether the phoneNumber field is set.
   */
  boolean hasPhoneNumber();
  /**
   * <code>required int32 phoneNumber = 3;</code>
   * @return The phoneNumber.
   */
  int getPhoneNumber();

  /**
   * <code>required .model.Timestamp birthDate = 4;</code>
   * @return Whether the birthDate field is set.
   */
  boolean hasBirthDate();
  /**
   * <code>required .model.Timestamp birthDate = 4;</code>
   * @return The birthDate.
   */
  uc.mei.is.protos.Timestamp getBirthDate();
  /**
   * <code>required .model.Timestamp birthDate = 4;</code>
   */
  uc.mei.is.protos.TimestampOrBuilder getBirthDateOrBuilder();

  /**
   * <code>required string address = 5;</code>
   * @return Whether the address field is set.
   */
  boolean hasAddress();
  /**
   * <code>required string address = 5;</code>
   * @return The address.
   */
  java.lang.String getAddress();
  /**
   * <code>required string address = 5;</code>
   * @return The bytes for address.
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <code>repeated .model.Student students = 6;</code>
   */
  java.util.List<uc.mei.is.protos.Student> 
      getStudentsList();
  /**
   * <code>repeated .model.Student students = 6;</code>
   */
  uc.mei.is.protos.Student getStudents(int index);
  /**
   * <code>repeated .model.Student students = 6;</code>
   */
  int getStudentsCount();
  /**
   * <code>repeated .model.Student students = 6;</code>
   */
  java.util.List<? extends uc.mei.is.protos.StudentOrBuilder> 
      getStudentsOrBuilderList();
  /**
   * <code>repeated .model.Student students = 6;</code>
   */
  uc.mei.is.protos.StudentOrBuilder getStudentsOrBuilder(
      int index);
}
