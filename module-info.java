module com.fasterxml.jackson.databind {
  requires java.desktop;
  requires java.logging;
  requires transitive com.fasterxml.jackson.annotation;
  requires transitive com.fasterxml.jackson.core;
  requires java.sql;
  requires java.xml;
  requires java.base;
  
  exports com.fasterxml.jackson.databind;
  exports com.fasterxml.jackson.databind.annotation;
  exports com.fasterxml.jackson.databind.cfg;
  exports com.fasterxml.jackson.databind.deser;
  exports com.fasterxml.jackson.databind.deser.impl;
  exports com.fasterxml.jackson.databind.deser.std;
  exports com.fasterxml.jackson.databind.exc;
  exports com.fasterxml.jackson.databind.ext;
  exports com.fasterxml.jackson.databind.introspect;
  exports com.fasterxml.jackson.databind.json;
  exports com.fasterxml.jackson.databind.jsonFormatVisitors;
  exports com.fasterxml.jackson.databind.jsonschema;
  exports com.fasterxml.jackson.databind.jsontype;
  exports com.fasterxml.jackson.databind.jsontype.impl;
  exports com.fasterxml.jackson.databind.module;
  exports com.fasterxml.jackson.databind.node;
  exports com.fasterxml.jackson.databind.ser;
  exports com.fasterxml.jackson.databind.ser.impl;
  exports com.fasterxml.jackson.databind.ser.std;
  exports com.fasterxml.jackson.databind.type;
  exports com.fasterxml.jackson.databind.util;
  
  uses com.fasterxml.jackson.databind.Module;
  
  provides com.fasterxml.jackson.core.ObjectCodec with com.fasterxml.jackson.databind.ObjectMapper;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\module-info.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */