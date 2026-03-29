/// <reference types="tree-sitter-cli/dsl" />
// @ts-check
//
// Tree-sitter grammar for IBM HLASM (High Level Assembler)
// Based on the HLASM V1R6 Language Reference (SC26-4940-07)
//
// HLASM uses a column-oriented format:
//   Columns  1-71:  Statement field (begin=1, end=71)
//   Column  72:     Continuation-indicator field
//   Columns 73-80:  Identification-sequence field
//
// Statement format: [name] operation [operand[,operand]...] [remarks]
//
// Keyword detection is handled through tree-sitter queries (highlights.scm)
// rather than grammar rules, since HLASM keywords and ordinary symbols use
// the same character set and tree-sitter's lexer cannot distinguish them.

module.exports = grammar({
  name: 'hlasm',

  extras: $ => [/[ \t]/],

  conflicts: $ => [
    [$.label, $.operation],
  ],

  rules: {
    source_file: $ => repeat($._line),

    _line: $ => choice(
      $.comment_line,
      $.macro_comment_line,
      $.process_statement,
      $.instruction_statement,
      $.blank_line,
    ),

    blank_line: $ => '\n',

    // =========================================================
    // COMMENTS
    // =========================================================

    // Full-line comment: asterisk in column 1
    comment_line: $ => seq(
      '*',
      optional($.comment_text),
      '\n',
    ),

    comment_text: $ => /[^\n]+/,

    // Internal macro definition comment: .* in begin column
    macro_comment_line: $ => seq(
      token(seq('.', '*')),
      optional($.comment_text),
      '\n',
    ),

    // =========================================================
    // *PROCESS STATEMENT
    // =========================================================

    process_statement: $ => seq(
      /\*[Pp][Rr][Oo][Cc][Ee][Ss][Ss]/,
      optional($.process_operands),
      '\n',
    ),

    process_operands: $ => /[^\n]+/,

    // =========================================================
    // INSTRUCTION STATEMENT
    // =========================================================
    // Format: [name] operation [operands]
    // The newline terminates the statement. At EOF, the newline
    // may be absent -- tree-sitter handles this gracefully with
    // error recovery.

    instruction_statement: $ => seq(
      optional(field('label', $.label)),
      field('operation', $.operation),
      optional(field('operands', $.operands)),
      '\n',
    ),

    label: $ => choice(
      $.symbol,
      $.sequence_symbol,
      $.variable_symbol,
    ),

    operation: $ => $.symbol,

    // =========================================================
    // OPERANDS
    // =========================================================

    operands: $ => seq(
      $._operand,
      repeat(seq(',', $._operand)),
    ),

    _operand: $ => choice(
      $.address_operand,
      $.expression,
      $.string_literal,
      $.dc_operand,
    ),

    // Address operand: D(X,B) or D(,B) or D(B) or (X,B) or (,B)
    address_operand: $ => prec(2, choice(
      // D(X,B) -- displacement with index and base
      seq($._term, '(', $.expression, ',', $.expression, ')'),
      // D(,B) -- displacement with base only
      seq($._term, '(', ',', $.expression, ')'),
      // D(B) -- displacement with single base
      seq($._term, '(', $.expression, ')'),
      // (X,B) -- no displacement, index and base
      seq('(', $.expression, ',', $.expression, ')'),
      // (,B) -- no displacement, base only
      seq('(', ',', $.expression, ')'),
    )),

    // DC/DS operand: [dup]Type[Ext][Ln]['value'] or [dup]Type[Ext][Ln][(expr,...)]
    dc_operand: $ => prec(3, seq(
      field('type_spec', $.dc_type_spec),
      optional(field('value', $.dc_value)),
    )),

    // Matches: [digits]TypeLetter[ExtLetter][Ldigits]
    // e.g., "CL80", "F", "3F", "PL4", "XL2", "AD", "H", "0H"
    dc_type_spec: $ => token(seq(
      optional(/[0-9]+/),                                           // duplication factor
      /[AaBbCcDdEeFfGgHhJjPpQqRrSsVvXxYyZz]/,                    // type
      optional(/[AaDdHhBbEeUuLl]/),                                // type extension
      optional(seq(/[Ll]/, /[0-9]+/)),                              // length modifier
    )),

    dc_value: $ => choice(
      token(seq("'", /([^'\n]|'')*/, "'")),
      seq('(', $.expression, repeat(seq(',', $.expression)), ')'),
    ),

    // =========================================================
    // EXPRESSIONS
    // =========================================================

    expression: $ => choice(
      $._term,
      $.binary_expression,
      $.unary_expression,
      $.parenthesized_expression,
    ),

    binary_expression: $ => choice(
      prec.left(2, seq($.expression, '+', $.expression)),
      prec.left(2, seq($.expression, '-', $.expression)),
      prec.left(3, seq($.expression, '*', $.expression)),
      prec.left(3, seq($.expression, '/', $.expression)),
    ),

    unary_expression: $ => prec(4, seq(
      choice('+', '-'),
      $.expression,
    )),

    parenthesized_expression: $ => prec(1, seq(
      '(',
      $.expression,
      ')',
    )),

    // =========================================================
    // TERMS
    // =========================================================

    _term: $ => choice(
      $.symbol,
      $.variable_symbol,
      $.number,
      $.self_defining_term,
      $.location_counter,
      $.attribute_reference,
      $.literal,
    ),

    // Ordinary symbol: up to 63 alphanumeric chars, first alphabetic
    // Alphabetic includes: A-Z, a-z, @, $, #, _
    symbol: $ => /[a-zA-Z@$#_][a-zA-Z@$#_0-9]*/,

    // Variable symbol: &name
    variable_symbol: $ => seq(
      '&',
      token.immediate(/[a-zA-Z@$#_][a-zA-Z@$#_0-9]*/),
    ),

    // Sequence symbol: .name (used in macro branching)
    sequence_symbol: $ => seq(
      '.',
      token.immediate(/[a-zA-Z@$#_][a-zA-Z@$#_0-9]*/),
    ),

    // Decimal number
    number: $ => /[0-9]+/,

    // Location counter reference (asterisk)
    location_counter: $ => '*',

    // =========================================================
    // SELF-DEFINING TERMS
    // =========================================================

    self_defining_term: $ => choice(
      $.hex_self_defining_term,
      $.binary_self_defining_term,
      $.character_self_defining_term,
      $.graphic_self_defining_term,
    ),

    // X'hexdigits' -- lower precedence so dc_type_spec wins in DC/DS context
    hex_self_defining_term: $ => token(prec(-1, seq(
      /[Xx]/,
      "'",
      /[0-9a-fA-F]+/,
      "'",
    ))),

    // B'bits'
    binary_self_defining_term: $ => token(prec(-1, seq(
      /[Bb]/,
      "'",
      /[01]+/,
      "'",
    ))),

    // C'characters'
    character_self_defining_term: $ => token(prec(-1, seq(
      /[Cc]/,
      "'",
      /([^'\n]|'')*/,
      "'",
    ))),

    // G'<double-byte-chars>' (DBCS option)
    graphic_self_defining_term: $ => token(prec(-1, seq(
      /[Gg]/,
      "'",
      /([^'\n]|'')*/,
      "'",
    ))),

    // =========================================================
    // LITERALS
    // =========================================================
    // Literal: =type'value' (e.g. =F'200', =A(*), =H'0', =3A(*))

    literal: $ => seq(
      '=',
      $.dc_type_spec,
      $.dc_value,
    ),

    // =========================================================
    // ATTRIBUTE REFERENCES
    // =========================================================
    // L'symbol, T'symbol, S'symbol, I'symbol, K'symbol, etc.

    attribute_reference: $ => seq(
      /[LlTtSsIiKkNnDdOo]/,
      token.immediate("'"),
      choice($.symbol, $.variable_symbol, $.location_counter),
    ),

    // =========================================================
    // STRING LITERALS
    // =========================================================

    string_literal: $ => token(seq(
      "'",
      /([^'\n]|'')*/,
      "'",
    )),
  },
});
