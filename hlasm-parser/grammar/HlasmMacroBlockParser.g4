grammar HlasmMacroBlockParser;
import HlasmMacroBlockLexer;
@header {package com.mojo.hlasm;}

options {tokenVocab = HlasmMacroBlockLexer;}

startRule: statement+;
//section: labelStart statement* labelEnd;
statement: ifBlock | asm | comment | sql | raw | labelStart | labelEnd | macro | call;
ifBlock: if body elseBlock? endIf;
//elseIfBlock: else ifBlock;
elseBlock: else body;
body: statement*;
labelStart: LABELSTART UNDERSCORE GUID UNDERSCORE;
labelEnd: LABELEND UNDERSCORE GUID UNDERSCORE;
endIf: ENDIF UNDERSCORE GUID UNDERSCORE;
if: IF UNDERSCORE GUID UNDERSCORE;
else: ELSE UNDERSCORE GUID UNDERSCORE;
raw: RAW UNDERSCORE GUID UNDERSCORE;
asm: ASM UNDERSCORE GUID UNDERSCORE;
comment: COMMENT UNDERSCORE GUID UNDERSCORE;
sql: SQL UNDERSCORE GUID UNDERSCORE;
macro: MACRO UNDERSCORE GUID UNDERSCORE;
call: CALL UNDERSCORE GUID UNDERSCORE;
