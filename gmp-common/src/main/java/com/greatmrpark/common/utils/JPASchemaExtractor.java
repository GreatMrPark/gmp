package com.greatmrpark.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import javax.persistence.Entity;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 데이터 베이스 스키마 추출
 */
public class JPASchemaExtractor {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect")
                        .build());

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));


        for (BeanDefinition bd : scanner.findCandidateComponents("com.greatmrpark.helper.common.model.db")) {
            String name = bd.getBeanClassName();
            try {
                System.out.println("Added annotated entity class " + bd.getBeanClassName());
                metadata.addAnnotatedClass(Class.forName(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SchemaExport export = new SchemaExport();

        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT);
        
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd");
        Date time = new Date();
        String d = sdf.format(time);

        export.setDelimiter(";");
        export.setOutputFile("database/gelix_"+d+"_create.sql");
        export.setFormat(true);
        export.execute(targetTypes, SchemaExport.Action.BOTH, metadata.buildMetadata());
    }
}