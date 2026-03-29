package com.mojo.loader.main;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.Db2ASTBuilder;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.RawCodeElement;

public class DB2SqlGrammarDemoMain {
    public static void main(String[] args) {
        CodeElement parsed2 = new Db2ASTBuilder(new UUIDProvider()).run(new RawCodeElement("1", "SELECT FIRSTNAME, LASTNAME\n" +
                "FROM EMPLOYEE\n" +
                "WHERE DEPARTMENT = 'SALES'\n" +
                "FETCH FIRST 10 ROWS ONLY;"));
        System.out.println("COMPLETE");
    }
}
