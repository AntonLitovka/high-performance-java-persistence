package com.vladmihalcea.book.hpjp.jdbc.batch.generatedkeys.sequence;

/**
 * OracleSequenceCallTest - Oracle sequence call
 *
 * @author Vlad Mihalcea
 */
public class OracleSequenceCallTest extends AbstractSequenceCallTest {

    @Override
    protected String callSequenceSyntax() {
        return "select post_seq.NEXTVAL from dual";
    }

    @Override
    protected DataSourceProvider dataSourceProvider() {
        return new OracleDataSourceProvider();
    }
}
