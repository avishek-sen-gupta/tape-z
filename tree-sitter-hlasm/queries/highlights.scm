; Tree-sitter highlights query for HLASM
; Uses text matching on operation symbols to identify instruction types

; =========================================================
; COMMENTS
; =========================================================

(comment_line) @comment
(macro_comment_line) @comment
(comment_text) @comment

; =========================================================
; LABELS
; =========================================================

(label (symbol) @label)
(label (sequence_symbol) @label)
(label (variable_symbol) @label)

; =========================================================
; OPERATIONS - Assembler directives
; =========================================================

((operation (symbol) @keyword)
 (#match? @keyword "^(CSECT|DSECT|RSECT|COM|LOCTR|DXD|CXD|START|END|ENTRY|EXTRN|WXTRN)$"))

((operation (symbol) @keyword)
 (#match? @keyword "^(csect|dsect|rsect|com|loctr|dxd|cxd|start|end|entry|extrn|wxtrn)$"))

; Data definition
((operation (symbol) @keyword)
 (#match? @keyword "^[Dd][CcSs]$"))

; Symbol definition
((operation (symbol) @keyword)
 (#match? @keyword "^(EQU|equ|Equ)$"))

; Base register management
((operation (symbol) @keyword)
 (#match? @keyword "^(USING|DROP|using|drop|Using|Drop)$"))

; Program structure
((operation (symbol) @keyword)
 (#match? @keyword "^(COPY|ORG|LTORG|CNOP|AMODE|RMODE|copy|org|ltorg|cnop|amode|rmode)$"))

; Listing control
((operation (symbol) @keyword)
 (#match? @keyword "^(TITLE|PRINT|PUSH|POP|SPACE|EJECT|title|print|push|pop|space|eject)$"))

; Output instructions
((operation (symbol) @keyword)
 (#match? @keyword "^(MNOTE|PUNCH|REPRO|mnote|punch|repro)$"))

; Miscellaneous assembler
((operation (symbol) @keyword)
 (#match? @keyword "^(ALIAS|CATTR|XATTR|OPSYN|ICTL|ISEQ|ACONTROL|ADATA|AINSERT|EXITCTL|CCW|CCW0|CCW1)$"))

; =========================================================
; OPERATIONS - Macro instructions
; =========================================================

((operation (symbol) @keyword.control)
 (#match? @keyword.control "^(MACRO|MEND|MEXIT|macro|mend|mexit)$"))

; =========================================================
; OPERATIONS - Conditional assembly
; =========================================================

((operation (symbol) @keyword.control)
 (#match? @keyword.control "^(AIF|AIFB|AGO|AGOB|ANOP|ACTR|aif|aifb|ago|agob|anop|actr)$"))

((operation (symbol) @keyword.control)
 (#match? @keyword.control "^(SETA|SETB|SETC|SETAF|SETCF|seta|setb|setc|setaf|setcf)$"))

((operation (symbol) @keyword.control)
 (#match? @keyword.control "^(GBLA|GBLB|GBLC|LCLA|LCLB|LCLC|gbla|gblb|gblc|lcla|lclb|lclc)$"))

((operation (symbol) @keyword.control)
 (#match? @keyword.control "^(MHELP|AREAD|ASPACE|AEJECT|mhelp|aread|aspace|aeject)$"))

; =========================================================
; OPERATIONS - Machine instructions (catch-all)
; =========================================================

; Generic machine instruction mnemonic (if not matched above)
(operation (symbol) @function.builtin)

; =========================================================
; OPERANDS
; =========================================================

; Symbols in operands
(expression (symbol) @variable)

; Numbers
(number) @number

; Location counter
(location_counter) @constant.builtin

; Variable symbols
(variable_symbol) @variable.parameter

; Sequence symbols
(sequence_symbol) @label

; =========================================================
; SELF-DEFINING TERMS
; =========================================================

(hex_self_defining_term) @number
(binary_self_defining_term) @number
(character_self_defining_term) @string
(graphic_self_defining_term) @string

; =========================================================
; DC/DS OPERANDS
; =========================================================

(dc_type_spec) @type
(dc_value) @string

; =========================================================
; LITERALS
; =========================================================

(literal) @string.special

; =========================================================
; ATTRIBUTE REFERENCES
; =========================================================

(attribute_reference) @function.builtin

; =========================================================
; STRING LITERALS
; =========================================================

(string_literal) @string

; =========================================================
; PROCESS STATEMENT
; =========================================================

(process_statement) @keyword.directive
