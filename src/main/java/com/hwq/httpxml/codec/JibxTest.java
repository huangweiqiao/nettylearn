package com.hwq.httpxml.codec;

import com.hwq.httpxml.pojo.Student;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by weiqiao.huang on 2019/4/22.
 */
public class JibxTest {
    private IBindingFactory factory = null;
    private StringWriter writer=null;
    private StringReader reader = null;
    private String encode2Xml(Student student) throws JiBXException, IOException {
        String result="";
        factory =  BindingDirectory.getFactory(Student.class);
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(student,"UTF-8",null,writer);
        result =  writer.toString();
        writer.close();
        System.out.println("studentXml:"+result);
        return result;
    }

    private Student decode2Student(String xml) throws JiBXException {
        Student student = null;
        reader = new StringReader(xml);
        IUnmarshallingContext mctx =  factory.createUnmarshallingContext();
        student = (Student)mctx.unmarshalDocument(reader);
        System.out.println("student:"+student);
        reader.close();
        return student;
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setAge(20);
        student.setAddress("广州");
        student.setName("张三");
        JibxTest test = new JibxTest();
        try {
           String xml =  test.encode2Xml(student);
            test.decode2Student(xml);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
