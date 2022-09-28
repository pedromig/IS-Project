// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: model.proto

package uc.mei.is.model.proto;

/**
 * Protobuf type {@code model.Teacher}
 */
public final class Teacher extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:model.Teacher)
    TeacherOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Teacher.newBuilder() to construct.
  private Teacher(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Teacher() {
    student_ = emptyIntList();
    name_ = "";
    birthDate_ = "";
    address_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Teacher();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Teacher(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {
            bitField0_ |= 0x00000001;
            id_ = input.readInt32();
            break;
          }
          case 16: {
            if (!((mutable_bitField0_ & 0x00000002) != 0)) {
              student_ = newIntList();
              mutable_bitField0_ |= 0x00000002;
            }
            student_.addInt(input.readInt32());
            break;
          }
          case 18: {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (!((mutable_bitField0_ & 0x00000002) != 0) && input.getBytesUntilLimit() > 0) {
              student_ = newIntList();
              mutable_bitField0_ |= 0x00000002;
            }
            while (input.getBytesUntilLimit() > 0) {
              student_.addInt(input.readInt32());
            }
            input.popLimit(limit);
            break;
          }
          case 26: {
            com.google.protobuf.ByteString bs = input.readBytes();
            bitField0_ |= 0x00000002;
            name_ = bs;
            break;
          }
          case 32: {
            bitField0_ |= 0x00000004;
            phoneNumber_ = input.readInt32();
            break;
          }
          case 42: {
            com.google.protobuf.ByteString bs = input.readBytes();
            bitField0_ |= 0x00000008;
            birthDate_ = bs;
            break;
          }
          case 50: {
            com.google.protobuf.ByteString bs = input.readBytes();
            bitField0_ |= 0x00000010;
            address_ = bs;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000002) != 0)) {
        student_.makeImmutable(); // C
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return uc.mei.is.model.proto.Model.internal_static_model_Teacher_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return uc.mei.is.model.proto.Model.internal_static_model_Teacher_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            uc.mei.is.model.proto.Teacher.class, uc.mei.is.model.proto.Teacher.Builder.class);
  }

  private int bitField0_;
  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>required int32 id = 1;</code>
   * @return Whether the id field is set.
   */
  @java.lang.Override
  public boolean hasId() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>required int32 id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public int getId() {
    return id_;
  }

  public static final int STUDENT_FIELD_NUMBER = 2;
  private com.google.protobuf.Internal.IntList student_;
  /**
   * <code>repeated int32 student = 2;</code>
   * @return A list containing the student.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer>
      getStudentList() {
    return student_;
  }
  /**
   * <code>repeated int32 student = 2;</code>
   * @return The count of student.
   */
  public int getStudentCount() {
    return student_.size();
  }
  /**
   * <code>repeated int32 student = 2;</code>
   * @param index The index of the element to return.
   * @return The student at the given index.
   */
  public int getStudent(int index) {
    return student_.getInt(index);
  }

  public static final int NAME_FIELD_NUMBER = 3;
  private volatile java.lang.Object name_;
  /**
   * <code>required string name = 3;</code>
   * @return Whether the name field is set.
   */
  @java.lang.Override
  public boolean hasName() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>required string name = 3;</code>
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (bs.isValidUtf8()) {
        name_ = s;
      }
      return s;
    }
  }
  /**
   * <code>required string name = 3;</code>
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PHONENUMBER_FIELD_NUMBER = 4;
  private int phoneNumber_;
  /**
   * <code>required int32 phoneNumber = 4;</code>
   * @return Whether the phoneNumber field is set.
   */
  @java.lang.Override
  public boolean hasPhoneNumber() {
    return ((bitField0_ & 0x00000004) != 0);
  }
  /**
   * <code>required int32 phoneNumber = 4;</code>
   * @return The phoneNumber.
   */
  @java.lang.Override
  public int getPhoneNumber() {
    return phoneNumber_;
  }

  public static final int BIRTHDATE_FIELD_NUMBER = 5;
  private volatile java.lang.Object birthDate_;
  /**
   * <code>required string birthDate = 5;</code>
   * @return Whether the birthDate field is set.
   */
  @java.lang.Override
  public boolean hasBirthDate() {
    return ((bitField0_ & 0x00000008) != 0);
  }
  /**
   * <code>required string birthDate = 5;</code>
   * @return The birthDate.
   */
  @java.lang.Override
  public java.lang.String getBirthDate() {
    java.lang.Object ref = birthDate_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (bs.isValidUtf8()) {
        birthDate_ = s;
      }
      return s;
    }
  }
  /**
   * <code>required string birthDate = 5;</code>
   * @return The bytes for birthDate.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getBirthDateBytes() {
    java.lang.Object ref = birthDate_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      birthDate_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ADDRESS_FIELD_NUMBER = 6;
  private volatile java.lang.Object address_;
  /**
   * <code>required string address = 6;</code>
   * @return Whether the address field is set.
   */
  @java.lang.Override
  public boolean hasAddress() {
    return ((bitField0_ & 0x00000010) != 0);
  }
  /**
   * <code>required string address = 6;</code>
   * @return The address.
   */
  @java.lang.Override
  public java.lang.String getAddress() {
    java.lang.Object ref = address_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (bs.isValidUtf8()) {
        address_ = s;
      }
      return s;
    }
  }
  /**
   * <code>required string address = 6;</code>
   * @return The bytes for address.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAddressBytes() {
    java.lang.Object ref = address_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      address_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    if (!hasId()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasName()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasPhoneNumber()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasBirthDate()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasAddress()) {
      memoizedIsInitialized = 0;
      return false;
    }
    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeInt32(1, id_);
    }
    for (int i = 0; i < student_.size(); i++) {
      output.writeInt32(2, student_.getInt(i));
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, name_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      output.writeInt32(4, phoneNumber_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, birthDate_);
    }
    if (((bitField0_ & 0x00000010) != 0)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 6, address_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < student_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt32SizeNoTag(student_.getInt(i));
      }
      size += dataSize;
      size += 1 * getStudentList().size();
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, name_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(4, phoneNumber_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, birthDate_);
    }
    if (((bitField0_ & 0x00000010) != 0)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(6, address_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof uc.mei.is.model.proto.Teacher)) {
      return super.equals(obj);
    }
    uc.mei.is.model.proto.Teacher other = (uc.mei.is.model.proto.Teacher) obj;

    if (hasId() != other.hasId()) return false;
    if (hasId()) {
      if (getId()
          != other.getId()) return false;
    }
    if (!getStudentList()
        .equals(other.getStudentList())) return false;
    if (hasName() != other.hasName()) return false;
    if (hasName()) {
      if (!getName()
          .equals(other.getName())) return false;
    }
    if (hasPhoneNumber() != other.hasPhoneNumber()) return false;
    if (hasPhoneNumber()) {
      if (getPhoneNumber()
          != other.getPhoneNumber()) return false;
    }
    if (hasBirthDate() != other.hasBirthDate()) return false;
    if (hasBirthDate()) {
      if (!getBirthDate()
          .equals(other.getBirthDate())) return false;
    }
    if (hasAddress() != other.hasAddress()) return false;
    if (hasAddress()) {
      if (!getAddress()
          .equals(other.getAddress())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasId()) {
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId();
    }
    if (getStudentCount() > 0) {
      hash = (37 * hash) + STUDENT_FIELD_NUMBER;
      hash = (53 * hash) + getStudentList().hashCode();
    }
    if (hasName()) {
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
    }
    if (hasPhoneNumber()) {
      hash = (37 * hash) + PHONENUMBER_FIELD_NUMBER;
      hash = (53 * hash) + getPhoneNumber();
    }
    if (hasBirthDate()) {
      hash = (37 * hash) + BIRTHDATE_FIELD_NUMBER;
      hash = (53 * hash) + getBirthDate().hashCode();
    }
    if (hasAddress()) {
      hash = (37 * hash) + ADDRESS_FIELD_NUMBER;
      hash = (53 * hash) + getAddress().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static uc.mei.is.model.proto.Teacher parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static uc.mei.is.model.proto.Teacher parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static uc.mei.is.model.proto.Teacher parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static uc.mei.is.model.proto.Teacher parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(uc.mei.is.model.proto.Teacher prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code model.Teacher}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:model.Teacher)
      uc.mei.is.model.proto.TeacherOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return uc.mei.is.model.proto.Model.internal_static_model_Teacher_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return uc.mei.is.model.proto.Model.internal_static_model_Teacher_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              uc.mei.is.model.proto.Teacher.class, uc.mei.is.model.proto.Teacher.Builder.class);
    }

    // Construct using uc.mei.is.model.proto.Teacher.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      id_ = 0;
      bitField0_ = (bitField0_ & ~0x00000001);
      student_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000002);
      name_ = "";
      bitField0_ = (bitField0_ & ~0x00000004);
      phoneNumber_ = 0;
      bitField0_ = (bitField0_ & ~0x00000008);
      birthDate_ = "";
      bitField0_ = (bitField0_ & ~0x00000010);
      address_ = "";
      bitField0_ = (bitField0_ & ~0x00000020);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return uc.mei.is.model.proto.Model.internal_static_model_Teacher_descriptor;
    }

    @java.lang.Override
    public uc.mei.is.model.proto.Teacher getDefaultInstanceForType() {
      return uc.mei.is.model.proto.Teacher.getDefaultInstance();
    }

    @java.lang.Override
    public uc.mei.is.model.proto.Teacher build() {
      uc.mei.is.model.proto.Teacher result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public uc.mei.is.model.proto.Teacher buildPartial() {
      uc.mei.is.model.proto.Teacher result = new uc.mei.is.model.proto.Teacher(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
        to_bitField0_ |= 0x00000001;
      }
      if (((bitField0_ & 0x00000002) != 0)) {
        student_.makeImmutable();
        bitField0_ = (bitField0_ & ~0x00000002);
      }
      result.student_ = student_;
      if (((from_bitField0_ & 0x00000004) != 0)) {
        to_bitField0_ |= 0x00000002;
      }
      result.name_ = name_;
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.phoneNumber_ = phoneNumber_;
        to_bitField0_ |= 0x00000004;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        to_bitField0_ |= 0x00000008;
      }
      result.birthDate_ = birthDate_;
      if (((from_bitField0_ & 0x00000020) != 0)) {
        to_bitField0_ |= 0x00000010;
      }
      result.address_ = address_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof uc.mei.is.model.proto.Teacher) {
        return mergeFrom((uc.mei.is.model.proto.Teacher)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(uc.mei.is.model.proto.Teacher other) {
      if (other == uc.mei.is.model.proto.Teacher.getDefaultInstance()) return this;
      if (other.hasId()) {
        setId(other.getId());
      }
      if (!other.student_.isEmpty()) {
        if (student_.isEmpty()) {
          student_ = other.student_;
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          ensureStudentIsMutable();
          student_.addAll(other.student_);
        }
        onChanged();
      }
      if (other.hasName()) {
        bitField0_ |= 0x00000004;
        name_ = other.name_;
        onChanged();
      }
      if (other.hasPhoneNumber()) {
        setPhoneNumber(other.getPhoneNumber());
      }
      if (other.hasBirthDate()) {
        bitField0_ |= 0x00000010;
        birthDate_ = other.birthDate_;
        onChanged();
      }
      if (other.hasAddress()) {
        bitField0_ |= 0x00000020;
        address_ = other.address_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      if (!hasId()) {
        return false;
      }
      if (!hasName()) {
        return false;
      }
      if (!hasPhoneNumber()) {
        return false;
      }
      if (!hasBirthDate()) {
        return false;
      }
      if (!hasAddress()) {
        return false;
      }
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      uc.mei.is.model.proto.Teacher parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (uc.mei.is.model.proto.Teacher) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int id_ ;
    /**
     * <code>required int32 id = 1;</code>
     * @return Whether the id field is set.
     */
    @java.lang.Override
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>required int32 id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public int getId() {
      return id_;
    }
    /**
     * <code>required int32 id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(int value) {
      bitField0_ |= 0x00000001;
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required int32 id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      id_ = 0;
      onChanged();
      return this;
    }

    private com.google.protobuf.Internal.IntList student_ = emptyIntList();
    private void ensureStudentIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        student_ = mutableCopy(student_);
        bitField0_ |= 0x00000002;
       }
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @return A list containing the student.
     */
    public java.util.List<java.lang.Integer>
        getStudentList() {
      return ((bitField0_ & 0x00000002) != 0) ?
               java.util.Collections.unmodifiableList(student_) : student_;
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @return The count of student.
     */
    public int getStudentCount() {
      return student_.size();
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @param index The index of the element to return.
     * @return The student at the given index.
     */
    public int getStudent(int index) {
      return student_.getInt(index);
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @param index The index to set the value at.
     * @param value The student to set.
     * @return This builder for chaining.
     */
    public Builder setStudent(
        int index, int value) {
      ensureStudentIsMutable();
      student_.setInt(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @param value The student to add.
     * @return This builder for chaining.
     */
    public Builder addStudent(int value) {
      ensureStudentIsMutable();
      student_.addInt(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @param values The student to add.
     * @return This builder for chaining.
     */
    public Builder addAllStudent(
        java.lang.Iterable<? extends java.lang.Integer> values) {
      ensureStudentIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, student_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 student = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearStudent() {
      student_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }

    private java.lang.Object name_ = "";
    /**
     * <code>required string name = 3;</code>
     * @return Whether the name field is set.
     */
    public boolean hasName() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>required string name = 3;</code>
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>required string name = 3;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>required string name = 3;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
      name_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required string name = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      bitField0_ = (bitField0_ & ~0x00000004);
      name_ = getDefaultInstance().getName();
      onChanged();
      return this;
    }
    /**
     * <code>required string name = 3;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
      name_ = value;
      onChanged();
      return this;
    }

    private int phoneNumber_ ;
    /**
     * <code>required int32 phoneNumber = 4;</code>
     * @return Whether the phoneNumber field is set.
     */
    @java.lang.Override
    public boolean hasPhoneNumber() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>required int32 phoneNumber = 4;</code>
     * @return The phoneNumber.
     */
    @java.lang.Override
    public int getPhoneNumber() {
      return phoneNumber_;
    }
    /**
     * <code>required int32 phoneNumber = 4;</code>
     * @param value The phoneNumber to set.
     * @return This builder for chaining.
     */
    public Builder setPhoneNumber(int value) {
      bitField0_ |= 0x00000008;
      phoneNumber_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required int32 phoneNumber = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearPhoneNumber() {
      bitField0_ = (bitField0_ & ~0x00000008);
      phoneNumber_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object birthDate_ = "";
    /**
     * <code>required string birthDate = 5;</code>
     * @return Whether the birthDate field is set.
     */
    public boolean hasBirthDate() {
      return ((bitField0_ & 0x00000010) != 0);
    }
    /**
     * <code>required string birthDate = 5;</code>
     * @return The birthDate.
     */
    public java.lang.String getBirthDate() {
      java.lang.Object ref = birthDate_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          birthDate_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>required string birthDate = 5;</code>
     * @return The bytes for birthDate.
     */
    public com.google.protobuf.ByteString
        getBirthDateBytes() {
      java.lang.Object ref = birthDate_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        birthDate_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>required string birthDate = 5;</code>
     * @param value The birthDate to set.
     * @return This builder for chaining.
     */
    public Builder setBirthDate(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
      birthDate_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required string birthDate = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearBirthDate() {
      bitField0_ = (bitField0_ & ~0x00000010);
      birthDate_ = getDefaultInstance().getBirthDate();
      onChanged();
      return this;
    }
    /**
     * <code>required string birthDate = 5;</code>
     * @param value The bytes for birthDate to set.
     * @return This builder for chaining.
     */
    public Builder setBirthDateBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
      birthDate_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object address_ = "";
    /**
     * <code>required string address = 6;</code>
     * @return Whether the address field is set.
     */
    public boolean hasAddress() {
      return ((bitField0_ & 0x00000020) != 0);
    }
    /**
     * <code>required string address = 6;</code>
     * @return The address.
     */
    public java.lang.String getAddress() {
      java.lang.Object ref = address_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          address_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>required string address = 6;</code>
     * @return The bytes for address.
     */
    public com.google.protobuf.ByteString
        getAddressBytes() {
      java.lang.Object ref = address_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        address_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>required string address = 6;</code>
     * @param value The address to set.
     * @return This builder for chaining.
     */
    public Builder setAddress(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
      address_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required string address = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearAddress() {
      bitField0_ = (bitField0_ & ~0x00000020);
      address_ = getDefaultInstance().getAddress();
      onChanged();
      return this;
    }
    /**
     * <code>required string address = 6;</code>
     * @param value The bytes for address to set.
     * @return This builder for chaining.
     */
    public Builder setAddressBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
      address_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:model.Teacher)
  }

  // @@protoc_insertion_point(class_scope:model.Teacher)
  private static final uc.mei.is.model.proto.Teacher DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new uc.mei.is.model.proto.Teacher();
  }

  public static uc.mei.is.model.proto.Teacher getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<Teacher>
      PARSER = new com.google.protobuf.AbstractParser<Teacher>() {
    @java.lang.Override
    public Teacher parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Teacher(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Teacher> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Teacher> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public uc.mei.is.model.proto.Teacher getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
