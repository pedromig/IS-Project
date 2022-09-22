// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package uc.mei.is.protos;

public interface StudentOrBuilder extends
    // @@protoc_insertion_point(interface_extends:model.Student)
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
   * <code>required string address = 4;</code>
   * @return Whether the address field is set.
   */
  boolean hasAddress();
  /**
   * <code>required string address = 4;</code>
   * @return The address.
   */
  java.lang.String getAddress();
  /**
   * <code>required string address = 4;</code>
   * @return The bytes for address.
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <code>required string gender = 5;</code>
   * @return Whether the gender field is set.
   */
  boolean hasGender();
  /**
   * <code>required string gender = 5;</code>
   * @return The gender.
   */
  java.lang.String getGender();
  /**
   * <code>required string gender = 5;</code>
   * @return The bytes for gender.
   */
  com.google.protobuf.ByteString
      getGenderBytes();

  /**
   * <code>required .model.Timestamp birthDate = 6;</code>
   * @return Whether the birthDate field is set.
   */
  boolean hasBirthDate();
  /**
   * <code>required .model.Timestamp birthDate = 6;</code>
   * @return The birthDate.
   */
  uc.mei.is.protos.Timestamp getBirthDate();
  /**
   * <code>required .model.Timestamp birthDate = 6;</code>
   */
  uc.mei.is.protos.TimestampOrBuilder getBirthDateOrBuilder();

  /**
   * <code>required .model.Timestamp registrationDate = 7;</code>
   * @return Whether the registrationDate field is set.
   */
  boolean hasRegistrationDate();
  /**
   * <code>required .model.Timestamp registrationDate = 7;</code>
   * @return The registrationDate.
   */
  uc.mei.is.protos.Timestamp getRegistrationDate();
  /**
   * <code>required .model.Timestamp registrationDate = 7;</code>
   */
  uc.mei.is.protos.TimestampOrBuilder getRegistrationDateOrBuilder();
}
