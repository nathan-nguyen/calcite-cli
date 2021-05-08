grammar CalciteShell;

tokens {
    DELIMITER
}

createTableWithValueCommand
    : TABLE (tableName = STRING_IDENTIFIER) COLUMNS columnName (',' columnName)* VALUES (valueBlock = VALUE_BLOCK)
    ;

columnName
    : STRING_IDENTIFIER
    ;

// Keywords
// Common Keywords
TABLE
    : T A B L E
    ;

COLUMNS
    : C O L U M N S
    ;

VALUES
    : V A L U E S
    ;

STRING_IDENTIFIER: '$'?[A-Za-z_][A-Za-z0-9_-]* | '`' (~'`')+ '`';

VALUE_BLOCK: '(' (~['('')'])* ')';

WHITESPACE: [ \t\n]+ -> skip;

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

fragment LETTER
    : [A-Z]
    ;

// Catch-all for anything we can't recognize.
// We use this to be able to ignore and recover all the text
// when splitting statements with DelimiterLexer
UNRECOGNIZED
    : .
    ;
