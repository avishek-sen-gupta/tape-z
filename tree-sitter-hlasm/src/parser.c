#include "tree_sitter/parser.h"

#if defined(__GNUC__) || defined(__clang__)
#pragma GCC diagnostic ignored "-Wmissing-field-initializers"
#endif

#define LANGUAGE_VERSION 14
#define STATE_COUNT 86
#define LARGE_STATE_COUNT 5
#define SYMBOL_COUNT 57
#define ALIAS_COUNT 0
#define TOKEN_COUNT 26
#define EXTERNAL_TOKEN_COUNT 0
#define FIELD_COUNT 5
#define MAX_ALIAS_SEQUENCE_LENGTH 6
#define PRODUCTION_ID_COUNT 7

enum ts_symbol_identifiers {
  anon_sym_LF = 1,
  anon_sym_STAR = 2,
  aux_sym_comment_text_token1 = 3,
  aux_sym_macro_comment_line_token1 = 4,
  aux_sym_process_statement_token1 = 5,
  anon_sym_COMMA = 6,
  anon_sym_LPAREN = 7,
  anon_sym_RPAREN = 8,
  sym_dc_type_spec = 9,
  aux_sym_dc_value_token1 = 10,
  anon_sym_PLUS = 11,
  anon_sym_DASH = 12,
  anon_sym_SLASH = 13,
  sym_symbol = 14,
  anon_sym_AMP = 15,
  aux_sym_variable_symbol_token1 = 16,
  anon_sym_DOT = 17,
  sym_number = 18,
  sym_hex_self_defining_term = 19,
  sym_binary_self_defining_term = 20,
  sym_character_self_defining_term = 21,
  sym_graphic_self_defining_term = 22,
  anon_sym_EQ = 23,
  aux_sym_attribute_reference_token1 = 24,
  anon_sym_SQUOTE = 25,
  sym_source_file = 26,
  sym__line = 27,
  sym_blank_line = 28,
  sym_comment_line = 29,
  sym_comment_text = 30,
  sym_macro_comment_line = 31,
  sym_process_statement = 32,
  sym_process_operands = 33,
  sym_instruction_statement = 34,
  sym_label = 35,
  sym_operation = 36,
  sym_operands = 37,
  sym__operand = 38,
  sym_address_operand = 39,
  sym_dc_operand = 40,
  sym_dc_value = 41,
  sym_expression = 42,
  sym_binary_expression = 43,
  sym_unary_expression = 44,
  sym_parenthesized_expression = 45,
  sym__term = 46,
  sym_variable_symbol = 47,
  sym_sequence_symbol = 48,
  sym_location_counter = 49,
  sym_self_defining_term = 50,
  sym_literal = 51,
  sym_attribute_reference = 52,
  sym_string_literal = 53,
  aux_sym_source_file_repeat1 = 54,
  aux_sym_operands_repeat1 = 55,
  aux_sym_dc_value_repeat1 = 56,
};

static const char * const ts_symbol_names[] = {
  [ts_builtin_sym_end] = "end",
  [anon_sym_LF] = "\n",
  [anon_sym_STAR] = "*",
  [aux_sym_comment_text_token1] = "comment_text_token1",
  [aux_sym_macro_comment_line_token1] = "macro_comment_line_token1",
  [aux_sym_process_statement_token1] = "process_statement_token1",
  [anon_sym_COMMA] = ",",
  [anon_sym_LPAREN] = "(",
  [anon_sym_RPAREN] = ")",
  [sym_dc_type_spec] = "dc_type_spec",
  [aux_sym_dc_value_token1] = "dc_value_token1",
  [anon_sym_PLUS] = "+",
  [anon_sym_DASH] = "-",
  [anon_sym_SLASH] = "/",
  [sym_symbol] = "symbol",
  [anon_sym_AMP] = "&",
  [aux_sym_variable_symbol_token1] = "variable_symbol_token1",
  [anon_sym_DOT] = ".",
  [sym_number] = "number",
  [sym_hex_self_defining_term] = "hex_self_defining_term",
  [sym_binary_self_defining_term] = "binary_self_defining_term",
  [sym_character_self_defining_term] = "character_self_defining_term",
  [sym_graphic_self_defining_term] = "graphic_self_defining_term",
  [anon_sym_EQ] = "=",
  [aux_sym_attribute_reference_token1] = "attribute_reference_token1",
  [anon_sym_SQUOTE] = "'",
  [sym_source_file] = "source_file",
  [sym__line] = "_line",
  [sym_blank_line] = "blank_line",
  [sym_comment_line] = "comment_line",
  [sym_comment_text] = "comment_text",
  [sym_macro_comment_line] = "macro_comment_line",
  [sym_process_statement] = "process_statement",
  [sym_process_operands] = "process_operands",
  [sym_instruction_statement] = "instruction_statement",
  [sym_label] = "label",
  [sym_operation] = "operation",
  [sym_operands] = "operands",
  [sym__operand] = "_operand",
  [sym_address_operand] = "address_operand",
  [sym_dc_operand] = "dc_operand",
  [sym_dc_value] = "dc_value",
  [sym_expression] = "expression",
  [sym_binary_expression] = "binary_expression",
  [sym_unary_expression] = "unary_expression",
  [sym_parenthesized_expression] = "parenthesized_expression",
  [sym__term] = "_term",
  [sym_variable_symbol] = "variable_symbol",
  [sym_sequence_symbol] = "sequence_symbol",
  [sym_location_counter] = "location_counter",
  [sym_self_defining_term] = "self_defining_term",
  [sym_literal] = "literal",
  [sym_attribute_reference] = "attribute_reference",
  [sym_string_literal] = "string_literal",
  [aux_sym_source_file_repeat1] = "source_file_repeat1",
  [aux_sym_operands_repeat1] = "operands_repeat1",
  [aux_sym_dc_value_repeat1] = "dc_value_repeat1",
};

static const TSSymbol ts_symbol_map[] = {
  [ts_builtin_sym_end] = ts_builtin_sym_end,
  [anon_sym_LF] = anon_sym_LF,
  [anon_sym_STAR] = anon_sym_STAR,
  [aux_sym_comment_text_token1] = aux_sym_comment_text_token1,
  [aux_sym_macro_comment_line_token1] = aux_sym_macro_comment_line_token1,
  [aux_sym_process_statement_token1] = aux_sym_process_statement_token1,
  [anon_sym_COMMA] = anon_sym_COMMA,
  [anon_sym_LPAREN] = anon_sym_LPAREN,
  [anon_sym_RPAREN] = anon_sym_RPAREN,
  [sym_dc_type_spec] = sym_dc_type_spec,
  [aux_sym_dc_value_token1] = aux_sym_dc_value_token1,
  [anon_sym_PLUS] = anon_sym_PLUS,
  [anon_sym_DASH] = anon_sym_DASH,
  [anon_sym_SLASH] = anon_sym_SLASH,
  [sym_symbol] = sym_symbol,
  [anon_sym_AMP] = anon_sym_AMP,
  [aux_sym_variable_symbol_token1] = aux_sym_variable_symbol_token1,
  [anon_sym_DOT] = anon_sym_DOT,
  [sym_number] = sym_number,
  [sym_hex_self_defining_term] = sym_hex_self_defining_term,
  [sym_binary_self_defining_term] = sym_binary_self_defining_term,
  [sym_character_self_defining_term] = sym_character_self_defining_term,
  [sym_graphic_self_defining_term] = sym_graphic_self_defining_term,
  [anon_sym_EQ] = anon_sym_EQ,
  [aux_sym_attribute_reference_token1] = aux_sym_attribute_reference_token1,
  [anon_sym_SQUOTE] = anon_sym_SQUOTE,
  [sym_source_file] = sym_source_file,
  [sym__line] = sym__line,
  [sym_blank_line] = sym_blank_line,
  [sym_comment_line] = sym_comment_line,
  [sym_comment_text] = sym_comment_text,
  [sym_macro_comment_line] = sym_macro_comment_line,
  [sym_process_statement] = sym_process_statement,
  [sym_process_operands] = sym_process_operands,
  [sym_instruction_statement] = sym_instruction_statement,
  [sym_label] = sym_label,
  [sym_operation] = sym_operation,
  [sym_operands] = sym_operands,
  [sym__operand] = sym__operand,
  [sym_address_operand] = sym_address_operand,
  [sym_dc_operand] = sym_dc_operand,
  [sym_dc_value] = sym_dc_value,
  [sym_expression] = sym_expression,
  [sym_binary_expression] = sym_binary_expression,
  [sym_unary_expression] = sym_unary_expression,
  [sym_parenthesized_expression] = sym_parenthesized_expression,
  [sym__term] = sym__term,
  [sym_variable_symbol] = sym_variable_symbol,
  [sym_sequence_symbol] = sym_sequence_symbol,
  [sym_location_counter] = sym_location_counter,
  [sym_self_defining_term] = sym_self_defining_term,
  [sym_literal] = sym_literal,
  [sym_attribute_reference] = sym_attribute_reference,
  [sym_string_literal] = sym_string_literal,
  [aux_sym_source_file_repeat1] = aux_sym_source_file_repeat1,
  [aux_sym_operands_repeat1] = aux_sym_operands_repeat1,
  [aux_sym_dc_value_repeat1] = aux_sym_dc_value_repeat1,
};

static const TSSymbolMetadata ts_symbol_metadata[] = {
  [ts_builtin_sym_end] = {
    .visible = false,
    .named = true,
  },
  [anon_sym_LF] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_STAR] = {
    .visible = true,
    .named = false,
  },
  [aux_sym_comment_text_token1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_macro_comment_line_token1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_process_statement_token1] = {
    .visible = false,
    .named = false,
  },
  [anon_sym_COMMA] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LPAREN] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_RPAREN] = {
    .visible = true,
    .named = false,
  },
  [sym_dc_type_spec] = {
    .visible = true,
    .named = true,
  },
  [aux_sym_dc_value_token1] = {
    .visible = false,
    .named = false,
  },
  [anon_sym_PLUS] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_DASH] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_SLASH] = {
    .visible = true,
    .named = false,
  },
  [sym_symbol] = {
    .visible = true,
    .named = true,
  },
  [anon_sym_AMP] = {
    .visible = true,
    .named = false,
  },
  [aux_sym_variable_symbol_token1] = {
    .visible = false,
    .named = false,
  },
  [anon_sym_DOT] = {
    .visible = true,
    .named = false,
  },
  [sym_number] = {
    .visible = true,
    .named = true,
  },
  [sym_hex_self_defining_term] = {
    .visible = true,
    .named = true,
  },
  [sym_binary_self_defining_term] = {
    .visible = true,
    .named = true,
  },
  [sym_character_self_defining_term] = {
    .visible = true,
    .named = true,
  },
  [sym_graphic_self_defining_term] = {
    .visible = true,
    .named = true,
  },
  [anon_sym_EQ] = {
    .visible = true,
    .named = false,
  },
  [aux_sym_attribute_reference_token1] = {
    .visible = false,
    .named = false,
  },
  [anon_sym_SQUOTE] = {
    .visible = true,
    .named = false,
  },
  [sym_source_file] = {
    .visible = true,
    .named = true,
  },
  [sym__line] = {
    .visible = false,
    .named = true,
  },
  [sym_blank_line] = {
    .visible = true,
    .named = true,
  },
  [sym_comment_line] = {
    .visible = true,
    .named = true,
  },
  [sym_comment_text] = {
    .visible = true,
    .named = true,
  },
  [sym_macro_comment_line] = {
    .visible = true,
    .named = true,
  },
  [sym_process_statement] = {
    .visible = true,
    .named = true,
  },
  [sym_process_operands] = {
    .visible = true,
    .named = true,
  },
  [sym_instruction_statement] = {
    .visible = true,
    .named = true,
  },
  [sym_label] = {
    .visible = true,
    .named = true,
  },
  [sym_operation] = {
    .visible = true,
    .named = true,
  },
  [sym_operands] = {
    .visible = true,
    .named = true,
  },
  [sym__operand] = {
    .visible = false,
    .named = true,
  },
  [sym_address_operand] = {
    .visible = true,
    .named = true,
  },
  [sym_dc_operand] = {
    .visible = true,
    .named = true,
  },
  [sym_dc_value] = {
    .visible = true,
    .named = true,
  },
  [sym_expression] = {
    .visible = true,
    .named = true,
  },
  [sym_binary_expression] = {
    .visible = true,
    .named = true,
  },
  [sym_unary_expression] = {
    .visible = true,
    .named = true,
  },
  [sym_parenthesized_expression] = {
    .visible = true,
    .named = true,
  },
  [sym__term] = {
    .visible = false,
    .named = true,
  },
  [sym_variable_symbol] = {
    .visible = true,
    .named = true,
  },
  [sym_sequence_symbol] = {
    .visible = true,
    .named = true,
  },
  [sym_location_counter] = {
    .visible = true,
    .named = true,
  },
  [sym_self_defining_term] = {
    .visible = true,
    .named = true,
  },
  [sym_literal] = {
    .visible = true,
    .named = true,
  },
  [sym_attribute_reference] = {
    .visible = true,
    .named = true,
  },
  [sym_string_literal] = {
    .visible = true,
    .named = true,
  },
  [aux_sym_source_file_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_operands_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_dc_value_repeat1] = {
    .visible = false,
    .named = false,
  },
};

enum ts_field_identifiers {
  field_label = 1,
  field_operands = 2,
  field_operation = 3,
  field_type_spec = 4,
  field_value = 5,
};

static const char * const ts_field_names[] = {
  [0] = NULL,
  [field_label] = "label",
  [field_operands] = "operands",
  [field_operation] = "operation",
  [field_type_spec] = "type_spec",
  [field_value] = "value",
};

static const TSFieldMapSlice ts_field_map_slices[PRODUCTION_ID_COUNT] = {
  [1] = {.index = 0, .length = 1},
  [2] = {.index = 1, .length = 1},
  [3] = {.index = 2, .length = 2},
  [4] = {.index = 4, .length = 2},
  [5] = {.index = 6, .length = 2},
  [6] = {.index = 8, .length = 3},
};

static const TSFieldMapEntry ts_field_map_entries[] = {
  [0] =
    {field_operation, 0},
  [1] =
    {field_type_spec, 0},
  [2] =
    {field_label, 0},
    {field_operation, 1},
  [4] =
    {field_type_spec, 0},
    {field_value, 1},
  [6] =
    {field_operands, 1},
    {field_operation, 0},
  [8] =
    {field_label, 0},
    {field_operands, 2},
    {field_operation, 1},
};

static const TSSymbol ts_alias_sequences[PRODUCTION_ID_COUNT][MAX_ALIAS_SEQUENCE_LENGTH] = {
  [0] = {0},
};

static const uint16_t ts_non_terminal_alias_map[] = {
  0,
};

static const TSStateId ts_primary_state_ids[STATE_COUNT] = {
  [0] = 0,
  [1] = 1,
  [2] = 2,
  [3] = 3,
  [4] = 4,
  [5] = 5,
  [6] = 6,
  [7] = 7,
  [8] = 8,
  [9] = 9,
  [10] = 10,
  [11] = 11,
  [12] = 12,
  [13] = 13,
  [14] = 14,
  [15] = 15,
  [16] = 16,
  [17] = 17,
  [18] = 18,
  [19] = 19,
  [20] = 20,
  [21] = 21,
  [22] = 22,
  [23] = 23,
  [24] = 24,
  [25] = 25,
  [26] = 26,
  [27] = 27,
  [28] = 28,
  [29] = 29,
  [30] = 30,
  [31] = 31,
  [32] = 32,
  [33] = 33,
  [34] = 34,
  [35] = 35,
  [36] = 36,
  [37] = 37,
  [38] = 38,
  [39] = 39,
  [40] = 40,
  [41] = 41,
  [42] = 42,
  [43] = 43,
  [44] = 44,
  [45] = 45,
  [46] = 46,
  [47] = 47,
  [48] = 48,
  [49] = 49,
  [50] = 50,
  [51] = 51,
  [52] = 52,
  [53] = 53,
  [54] = 54,
  [55] = 55,
  [56] = 56,
  [57] = 57,
  [58] = 58,
  [59] = 59,
  [60] = 60,
  [61] = 61,
  [62] = 62,
  [63] = 63,
  [64] = 64,
  [65] = 65,
  [66] = 66,
  [67] = 67,
  [68] = 68,
  [69] = 69,
  [70] = 70,
  [71] = 71,
  [72] = 72,
  [73] = 73,
  [74] = 74,
  [75] = 75,
  [76] = 76,
  [77] = 77,
  [78] = 78,
  [79] = 79,
  [80] = 80,
  [81] = 81,
  [82] = 82,
  [83] = 83,
  [84] = 84,
  [85] = 85,
};

static TSCharacterRange sym_dc_type_spec_character_set_1[] = {
  {'0', '9'}, {'A', 'H'}, {'J', 'J'}, {'P', 'S'}, {'V', 'V'}, {'X', 'Z'}, {'a', 'h'}, {'j', 'j'},
  {'p', 's'}, {'v', 'v'}, {'x', 'z'},
};

static TSCharacterRange aux_sym_attribute_reference_token1_character_set_1[] = {
  {'D', 'D'}, {'I', 'I'}, {'K', 'L'}, {'N', 'O'}, {'S', 'T'}, {'d', 'd'}, {'i', 'i'}, {'k', 'l'},
  {'n', 'o'}, {'s', 't'},
};

static bool ts_lex(TSLexer *lexer, TSStateId state) {
  START_LEXER();
  eof = lexer->eof(lexer);
  switch (state) {
    case 0:
      if (eof) ADVANCE(15);
      ADVANCE_MAP(
        '\n', 16,
        '&', 40,
        '\'', 50,
        '(', 24,
        ')', 25,
        '*', 18,
        '+', 35,
        ',', 23,
        '-', 36,
        '.', 46,
        '/', 37,
        '=', 49,
      );
      if (lookahead == '\t' ||
          lookahead == ' ') SKIP(14);
      if (lookahead == 'B' ||
          lookahead == 'b') ADVANCE(41);
      if (lookahead == 'C' ||
          lookahead == 'c') ADVANCE(41);
      if (lookahead == 'G' ||
          lookahead == 'g') ADVANCE(41);
      if (lookahead == 'X' ||
          lookahead == 'x') ADVANCE(41);
      if (lookahead == 'D' ||
          lookahead == 'S' ||
          lookahead == 'd' ||
          lookahead == 's') ADVANCE(41);
      if (lookahead == '#' ||
          lookahead == '$' ||
          lookahead == '@' ||
          lookahead == 'M' ||
          lookahead == 'U' ||
          lookahead == 'W' ||
          lookahead == '_' ||
          lookahead == 'm' ||
          lookahead == 'u' ||
          lookahead == 'w') ADVANCE(45);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(47);
      if (lookahead == 'I' ||
          ('K' <= lookahead && lookahead <= 'O') ||
          lookahead == 'T' ||
          lookahead == 'i' ||
          ('k' <= lookahead && lookahead <= 'o') ||
          lookahead == 't') ADVANCE(45);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(41);
      END_STATE();
    case 1:
      ADVANCE_MAP(
        '\n', 16,
        '&', 40,
        '\'', 4,
        '(', 24,
        ')', 25,
        '*', 17,
        '+', 35,
        ',', 23,
        '-', 36,
        '/', 37,
        '=', 49,
      );
      if (lookahead == '\t' ||
          lookahead == ' ') SKIP(1);
      if (lookahead == 'B' ||
          lookahead == 'b') ADVANCE(26);
      if (lookahead == 'C' ||
          lookahead == 'c') ADVANCE(26);
      if (lookahead == 'G' ||
          lookahead == 'g') ADVANCE(26);
      if (lookahead == 'X' ||
          lookahead == 'x') ADVANCE(26);
      if (lookahead == 'D' ||
          lookahead == 'S' ||
          lookahead == 'd' ||
          lookahead == 's') ADVANCE(26);
      if (lookahead == '#' ||
          lookahead == '$' ||
          lookahead == '@' ||
          lookahead == 'M' ||
          lookahead == 'U' ||
          lookahead == 'W' ||
          lookahead == '_' ||
          lookahead == 'm' ||
          lookahead == 'u' ||
          lookahead == 'w') ADVANCE(39);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(47);
      if (lookahead == 'I' ||
          ('K' <= lookahead && lookahead <= 'O') ||
          lookahead == 'T' ||
          lookahead == 'i' ||
          ('k' <= lookahead && lookahead <= 'o') ||
          lookahead == 't') ADVANCE(39);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(26);
      END_STATE();
    case 2:
      ADVANCE_MAP(
        '\n', 16,
        '&', 40,
        '(', 24,
        ')', 25,
        '*', 17,
        '+', 35,
        ',', 23,
        '-', 36,
        '/', 37,
        '=', 49,
      );
      if (lookahead == '\t' ||
          lookahead == ' ') SKIP(2);
      if (lookahead == 'B' ||
          lookahead == 'b') ADVANCE(39);
      if (lookahead == 'C' ||
          lookahead == 'c') ADVANCE(39);
      if (lookahead == 'G' ||
          lookahead == 'g') ADVANCE(39);
      if (lookahead == 'X' ||
          lookahead == 'x') ADVANCE(39);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(48);
      if (set_contains(aux_sym_attribute_reference_token1_character_set_1, 10, lookahead)) ADVANCE(39);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 3:
      if (lookahead == '\n') ADVANCE(16);
      if (lookahead == '\t' ||
          lookahead == ' ') ADVANCE(19);
      if (lookahead != 0) ADVANCE(20);
      END_STATE();
    case 4:
      if (lookahead == '\'') ADVANCE(34);
      if (lookahead != 0 &&
          lookahead != '\n') ADVANCE(4);
      END_STATE();
    case 5:
      if (lookahead == 'C' ||
          lookahead == 'c') ADVANCE(6);
      END_STATE();
    case 6:
      if (lookahead == 'E' ||
          lookahead == 'e') ADVANCE(10);
      END_STATE();
    case 7:
      if (lookahead == 'O' ||
          lookahead == 'o') ADVANCE(5);
      END_STATE();
    case 8:
      if (lookahead == 'R' ||
          lookahead == 'r') ADVANCE(7);
      END_STATE();
    case 9:
      if (lookahead == 'S' ||
          lookahead == 's') ADVANCE(22);
      END_STATE();
    case 10:
      if (lookahead == 'S' ||
          lookahead == 's') ADVANCE(9);
      END_STATE();
    case 11:
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(33);
      END_STATE();
    case 12:
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 13:
      if (eof) ADVANCE(15);
      if (lookahead == '\n') ADVANCE(16);
      if (lookahead == '&') ADVANCE(40);
      if (lookahead == '\'') ADVANCE(4);
      if (lookahead == '(') ADVANCE(24);
      if (lookahead == '*') ADVANCE(18);
      if (lookahead == ',') ADVANCE(23);
      if (lookahead == '.') ADVANCE(46);
      if (lookahead == '\t' ||
          lookahead == ' ') SKIP(13);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 14:
      if (eof) ADVANCE(15);
      ADVANCE_MAP(
        '\n', 16,
        '&', 40,
        '(', 24,
        ')', 25,
        '*', 18,
        '+', 35,
        ',', 23,
        '-', 36,
        '.', 46,
        '/', 37,
        '=', 49,
      );
      if (lookahead == '\t' ||
          lookahead == ' ') SKIP(14);
      if (lookahead == 'B' ||
          lookahead == 'b') ADVANCE(26);
      if (lookahead == 'C' ||
          lookahead == 'c') ADVANCE(26);
      if (lookahead == 'G' ||
          lookahead == 'g') ADVANCE(26);
      if (lookahead == 'X' ||
          lookahead == 'x') ADVANCE(26);
      if (lookahead == 'D' ||
          lookahead == 'S' ||
          lookahead == 'd' ||
          lookahead == 's') ADVANCE(26);
      if (lookahead == '#' ||
          lookahead == '$' ||
          lookahead == '@' ||
          lookahead == 'M' ||
          lookahead == 'U' ||
          lookahead == 'W' ||
          lookahead == '_' ||
          lookahead == 'm' ||
          lookahead == 'u' ||
          lookahead == 'w') ADVANCE(39);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(47);
      if (lookahead == 'I' ||
          ('K' <= lookahead && lookahead <= 'O') ||
          lookahead == 'T' ||
          lookahead == 'i' ||
          ('k' <= lookahead && lookahead <= 'o') ||
          lookahead == 't') ADVANCE(39);
      if (('A' <= lookahead && lookahead <= 'Z') ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(26);
      END_STATE();
    case 15:
      ACCEPT_TOKEN(ts_builtin_sym_end);
      END_STATE();
    case 16:
      ACCEPT_TOKEN(anon_sym_LF);
      END_STATE();
    case 17:
      ACCEPT_TOKEN(anon_sym_STAR);
      END_STATE();
    case 18:
      ACCEPT_TOKEN(anon_sym_STAR);
      if (lookahead == 'P' ||
          lookahead == 'p') ADVANCE(8);
      END_STATE();
    case 19:
      ACCEPT_TOKEN(aux_sym_comment_text_token1);
      if (lookahead == '\t' ||
          lookahead == ' ') ADVANCE(19);
      if (lookahead != 0 &&
          lookahead != '\t' &&
          lookahead != '\n') ADVANCE(20);
      END_STATE();
    case 20:
      ACCEPT_TOKEN(aux_sym_comment_text_token1);
      if (lookahead != 0 &&
          lookahead != '\n') ADVANCE(20);
      END_STATE();
    case 21:
      ACCEPT_TOKEN(aux_sym_macro_comment_line_token1);
      END_STATE();
    case 22:
      ACCEPT_TOKEN(aux_sym_process_statement_token1);
      END_STATE();
    case 23:
      ACCEPT_TOKEN(anon_sym_COMMA);
      END_STATE();
    case 24:
      ACCEPT_TOKEN(anon_sym_LPAREN);
      END_STATE();
    case 25:
      ACCEPT_TOKEN(anon_sym_RPAREN);
      END_STATE();
    case 26:
      ACCEPT_TOKEN(sym_dc_type_spec);
      ADVANCE_MAP(
        'L', 28,
        'l', 28,
        'A', 29,
        'B', 29,
        'D', 29,
        'E', 29,
        'H', 29,
        'U', 29,
        'a', 29,
        'b', 29,
        'd', 29,
        'e', 29,
        'h', 29,
        'u', 29,
      );
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('c' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 27:
      ACCEPT_TOKEN(sym_dc_type_spec);
      ADVANCE_MAP(
        'L', 31,
        'l', 31,
        'A', 30,
        'B', 30,
        'D', 30,
        'E', 30,
        'H', 30,
        'U', 30,
        'a', 30,
        'b', 30,
        'd', 30,
        'e', 30,
        'h', 30,
        'u', 30,
      );
      END_STATE();
    case 28:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(38);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(32);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 29:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(38);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 30:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(11);
      END_STATE();
    case 31:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(11);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(33);
      END_STATE();
    case 32:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(32);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 33:
      ACCEPT_TOKEN(sym_dc_type_spec);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(33);
      END_STATE();
    case 34:
      ACCEPT_TOKEN(aux_sym_dc_value_token1);
      if (lookahead == '\'') ADVANCE(4);
      END_STATE();
    case 35:
      ACCEPT_TOKEN(anon_sym_PLUS);
      END_STATE();
    case 36:
      ACCEPT_TOKEN(anon_sym_DASH);
      END_STATE();
    case 37:
      ACCEPT_TOKEN(anon_sym_SLASH);
      END_STATE();
    case 38:
      ACCEPT_TOKEN(sym_symbol);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(32);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 39:
      ACCEPT_TOKEN(sym_symbol);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(39);
      END_STATE();
    case 40:
      ACCEPT_TOKEN(anon_sym_AMP);
      END_STATE();
    case 41:
      ACCEPT_TOKEN(aux_sym_variable_symbol_token1);
      ADVANCE_MAP(
        'L', 42,
        'l', 42,
        'A', 43,
        'B', 43,
        'D', 43,
        'E', 43,
        'H', 43,
        'U', 43,
        'a', 43,
        'b', 43,
        'd', 43,
        'e', 43,
        'h', 43,
        'u', 43,
      );
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('c' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 42:
      ACCEPT_TOKEN(aux_sym_variable_symbol_token1);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(44);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(44);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 43:
      ACCEPT_TOKEN(aux_sym_variable_symbol_token1);
      if (lookahead == 'L' ||
          lookahead == 'l') ADVANCE(44);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 44:
      ACCEPT_TOKEN(aux_sym_variable_symbol_token1);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(44);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 45:
      ACCEPT_TOKEN(aux_sym_variable_symbol_token1);
      if (lookahead == '#' ||
          lookahead == '$' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('@' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(45);
      END_STATE();
    case 46:
      ACCEPT_TOKEN(anon_sym_DOT);
      if (lookahead == '*') ADVANCE(21);
      END_STATE();
    case 47:
      ACCEPT_TOKEN(sym_number);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(47);
      if (set_contains(sym_dc_type_spec_character_set_1, 11, lookahead)) ADVANCE(27);
      END_STATE();
    case 48:
      ACCEPT_TOKEN(sym_number);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(48);
      END_STATE();
    case 49:
      ACCEPT_TOKEN(anon_sym_EQ);
      END_STATE();
    case 50:
      ACCEPT_TOKEN(anon_sym_SQUOTE);
      END_STATE();
    default:
      return false;
  }
}

static const TSLexMode ts_lex_modes[STATE_COUNT] = {
  [0] = {.lex_state = 0},
  [1] = {.lex_state = 13},
  [2] = {.lex_state = 1},
  [3] = {.lex_state = 1},
  [4] = {.lex_state = 1},
  [5] = {.lex_state = 2},
  [6] = {.lex_state = 2},
  [7] = {.lex_state = 2},
  [8] = {.lex_state = 2},
  [9] = {.lex_state = 2},
  [10] = {.lex_state = 2},
  [11] = {.lex_state = 2},
  [12] = {.lex_state = 2},
  [13] = {.lex_state = 2},
  [14] = {.lex_state = 2},
  [15] = {.lex_state = 2},
  [16] = {.lex_state = 13},
  [17] = {.lex_state = 13},
  [18] = {.lex_state = 1},
  [19] = {.lex_state = 1},
  [20] = {.lex_state = 2},
  [21] = {.lex_state = 13},
  [22] = {.lex_state = 13},
  [23] = {.lex_state = 13},
  [24] = {.lex_state = 1},
  [25] = {.lex_state = 13},
  [26] = {.lex_state = 13},
  [27] = {.lex_state = 1},
  [28] = {.lex_state = 13},
  [29] = {.lex_state = 13},
  [30] = {.lex_state = 1},
  [31] = {.lex_state = 13},
  [32] = {.lex_state = 1},
  [33] = {.lex_state = 13},
  [34] = {.lex_state = 13},
  [35] = {.lex_state = 1},
  [36] = {.lex_state = 13},
  [37] = {.lex_state = 1},
  [38] = {.lex_state = 1},
  [39] = {.lex_state = 1},
  [40] = {.lex_state = 1},
  [41] = {.lex_state = 1},
  [42] = {.lex_state = 1},
  [43] = {.lex_state = 1},
  [44] = {.lex_state = 1},
  [45] = {.lex_state = 1},
  [46] = {.lex_state = 1},
  [47] = {.lex_state = 1},
  [48] = {.lex_state = 1},
  [49] = {.lex_state = 1},
  [50] = {.lex_state = 1},
  [51] = {.lex_state = 2},
  [52] = {.lex_state = 1},
  [53] = {.lex_state = 1},
  [54] = {.lex_state = 13},
  [55] = {.lex_state = 1},
  [56] = {.lex_state = 0},
  [57] = {.lex_state = 0},
  [58] = {.lex_state = 13},
  [59] = {.lex_state = 3},
  [60] = {.lex_state = 0},
  [61] = {.lex_state = 0},
  [62] = {.lex_state = 0},
  [63] = {.lex_state = 3},
  [64] = {.lex_state = 3},
  [65] = {.lex_state = 0},
  [66] = {.lex_state = 0},
  [67] = {.lex_state = 0},
  [68] = {.lex_state = 0},
  [69] = {.lex_state = 0},
  [70] = {.lex_state = 0},
  [71] = {.lex_state = 13},
  [72] = {.lex_state = 0},
  [73] = {.lex_state = 0},
  [74] = {.lex_state = 13},
  [75] = {.lex_state = 0},
  [76] = {.lex_state = 0},
  [77] = {.lex_state = 12},
  [78] = {.lex_state = 0},
  [79] = {.lex_state = 12},
  [80] = {.lex_state = 0},
  [81] = {.lex_state = 0},
  [82] = {.lex_state = 0},
  [83] = {.lex_state = 13},
  [84] = {.lex_state = 1},
  [85] = {.lex_state = 0},
};

static const uint16_t ts_parse_table[LARGE_STATE_COUNT][SYMBOL_COUNT] = {
  [0] = {
    [ts_builtin_sym_end] = ACTIONS(1),
    [anon_sym_LF] = ACTIONS(1),
    [anon_sym_STAR] = ACTIONS(1),
    [aux_sym_macro_comment_line_token1] = ACTIONS(1),
    [aux_sym_process_statement_token1] = ACTIONS(1),
    [anon_sym_COMMA] = ACTIONS(1),
    [anon_sym_LPAREN] = ACTIONS(1),
    [anon_sym_RPAREN] = ACTIONS(1),
    [sym_dc_type_spec] = ACTIONS(1),
    [anon_sym_PLUS] = ACTIONS(1),
    [anon_sym_DASH] = ACTIONS(1),
    [anon_sym_SLASH] = ACTIONS(1),
    [sym_symbol] = ACTIONS(1),
    [anon_sym_AMP] = ACTIONS(1),
    [aux_sym_variable_symbol_token1] = ACTIONS(1),
    [anon_sym_DOT] = ACTIONS(1),
    [sym_number] = ACTIONS(1),
    [sym_hex_self_defining_term] = ACTIONS(1),
    [sym_binary_self_defining_term] = ACTIONS(1),
    [sym_character_self_defining_term] = ACTIONS(1),
    [sym_graphic_self_defining_term] = ACTIONS(1),
    [anon_sym_EQ] = ACTIONS(1),
    [aux_sym_attribute_reference_token1] = ACTIONS(1),
    [anon_sym_SQUOTE] = ACTIONS(1),
  },
  [1] = {
    [sym_source_file] = STATE(76),
    [sym__line] = STATE(16),
    [sym_blank_line] = STATE(16),
    [sym_comment_line] = STATE(16),
    [sym_macro_comment_line] = STATE(16),
    [sym_process_statement] = STATE(16),
    [sym_instruction_statement] = STATE(16),
    [sym_label] = STATE(71),
    [sym_operation] = STATE(2),
    [sym_variable_symbol] = STATE(74),
    [sym_sequence_symbol] = STATE(74),
    [aux_sym_source_file_repeat1] = STATE(16),
    [ts_builtin_sym_end] = ACTIONS(3),
    [anon_sym_LF] = ACTIONS(5),
    [anon_sym_STAR] = ACTIONS(7),
    [aux_sym_macro_comment_line_token1] = ACTIONS(9),
    [aux_sym_process_statement_token1] = ACTIONS(11),
    [sym_symbol] = ACTIONS(13),
    [anon_sym_AMP] = ACTIONS(15),
    [anon_sym_DOT] = ACTIONS(17),
  },
  [2] = {
    [sym_operands] = STATE(85),
    [sym__operand] = STATE(60),
    [sym_address_operand] = STATE(60),
    [sym_dc_operand] = STATE(60),
    [sym_expression] = STATE(46),
    [sym_binary_expression] = STATE(40),
    [sym_unary_expression] = STATE(40),
    [sym_parenthesized_expression] = STATE(40),
    [sym__term] = STATE(39),
    [sym_variable_symbol] = STATE(39),
    [sym_location_counter] = STATE(39),
    [sym_self_defining_term] = STATE(39),
    [sym_literal] = STATE(39),
    [sym_attribute_reference] = STATE(39),
    [sym_string_literal] = STATE(60),
    [anon_sym_LF] = ACTIONS(19),
    [anon_sym_STAR] = ACTIONS(21),
    [anon_sym_LPAREN] = ACTIONS(23),
    [sym_dc_type_spec] = ACTIONS(25),
    [aux_sym_dc_value_token1] = ACTIONS(27),
    [anon_sym_PLUS] = ACTIONS(29),
    [anon_sym_DASH] = ACTIONS(29),
    [sym_symbol] = ACTIONS(31),
    [anon_sym_AMP] = ACTIONS(15),
    [sym_number] = ACTIONS(31),
    [sym_hex_self_defining_term] = ACTIONS(33),
    [sym_binary_self_defining_term] = ACTIONS(33),
    [sym_character_self_defining_term] = ACTIONS(33),
    [sym_graphic_self_defining_term] = ACTIONS(33),
    [anon_sym_EQ] = ACTIONS(35),
    [aux_sym_attribute_reference_token1] = ACTIONS(37),
  },
  [3] = {
    [sym_operands] = STATE(75),
    [sym__operand] = STATE(60),
    [sym_address_operand] = STATE(60),
    [sym_dc_operand] = STATE(60),
    [sym_expression] = STATE(46),
    [sym_binary_expression] = STATE(40),
    [sym_unary_expression] = STATE(40),
    [sym_parenthesized_expression] = STATE(40),
    [sym__term] = STATE(39),
    [sym_variable_symbol] = STATE(39),
    [sym_location_counter] = STATE(39),
    [sym_self_defining_term] = STATE(39),
    [sym_literal] = STATE(39),
    [sym_attribute_reference] = STATE(39),
    [sym_string_literal] = STATE(60),
    [anon_sym_LF] = ACTIONS(39),
    [anon_sym_STAR] = ACTIONS(21),
    [anon_sym_LPAREN] = ACTIONS(23),
    [sym_dc_type_spec] = ACTIONS(25),
    [aux_sym_dc_value_token1] = ACTIONS(27),
    [anon_sym_PLUS] = ACTIONS(29),
    [anon_sym_DASH] = ACTIONS(29),
    [sym_symbol] = ACTIONS(31),
    [anon_sym_AMP] = ACTIONS(15),
    [sym_number] = ACTIONS(31),
    [sym_hex_self_defining_term] = ACTIONS(33),
    [sym_binary_self_defining_term] = ACTIONS(33),
    [sym_character_self_defining_term] = ACTIONS(33),
    [sym_graphic_self_defining_term] = ACTIONS(33),
    [anon_sym_EQ] = ACTIONS(35),
    [aux_sym_attribute_reference_token1] = ACTIONS(37),
  },
  [4] = {
    [sym__operand] = STATE(69),
    [sym_address_operand] = STATE(69),
    [sym_dc_operand] = STATE(69),
    [sym_expression] = STATE(46),
    [sym_binary_expression] = STATE(40),
    [sym_unary_expression] = STATE(40),
    [sym_parenthesized_expression] = STATE(40),
    [sym__term] = STATE(39),
    [sym_variable_symbol] = STATE(39),
    [sym_location_counter] = STATE(39),
    [sym_self_defining_term] = STATE(39),
    [sym_literal] = STATE(39),
    [sym_attribute_reference] = STATE(39),
    [sym_string_literal] = STATE(69),
    [anon_sym_STAR] = ACTIONS(21),
    [anon_sym_LPAREN] = ACTIONS(23),
    [sym_dc_type_spec] = ACTIONS(25),
    [aux_sym_dc_value_token1] = ACTIONS(27),
    [anon_sym_PLUS] = ACTIONS(29),
    [anon_sym_DASH] = ACTIONS(29),
    [sym_symbol] = ACTIONS(31),
    [anon_sym_AMP] = ACTIONS(15),
    [sym_number] = ACTIONS(31),
    [sym_hex_self_defining_term] = ACTIONS(33),
    [sym_binary_self_defining_term] = ACTIONS(33),
    [sym_character_self_defining_term] = ACTIONS(33),
    [sym_graphic_self_defining_term] = ACTIONS(33),
    [anon_sym_EQ] = ACTIONS(35),
    [aux_sym_attribute_reference_token1] = ACTIONS(37),
  },
};

static const uint16_t ts_small_parse_table[] = {
  [0] = 11,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(41), 1,
      anon_sym_COMMA,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(47), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [47] = 11,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    ACTIONS(47), 1,
      anon_sym_COMMA,
    STATE(49), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [94] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(41), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [138] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(55), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [182] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(50), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [226] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(52), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [270] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(43), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [314] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(48), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [358] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(42), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [402] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(44), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [446] = 10,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(35), 1,
      anon_sym_EQ,
    ACTIONS(37), 1,
      aux_sym_attribute_reference_token1,
    ACTIONS(43), 1,
      anon_sym_LPAREN,
    STATE(53), 1,
      sym_expression,
    ACTIONS(29), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(45), 2,
      sym_symbol,
      sym_number,
    ACTIONS(33), 4,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
    STATE(40), 9,
      sym_binary_expression,
      sym_unary_expression,
      sym_parenthesized_expression,
      sym__term,
      sym_variable_symbol,
      sym_location_counter,
      sym_self_defining_term,
      sym_literal,
      sym_attribute_reference,
  [490] = 12,
    ACTIONS(5), 1,
      anon_sym_LF,
    ACTIONS(7), 1,
      anon_sym_STAR,
    ACTIONS(9), 1,
      aux_sym_macro_comment_line_token1,
    ACTIONS(11), 1,
      aux_sym_process_statement_token1,
    ACTIONS(13), 1,
      sym_symbol,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(17), 1,
      anon_sym_DOT,
    ACTIONS(49), 1,
      ts_builtin_sym_end,
    STATE(2), 1,
      sym_operation,
    STATE(71), 1,
      sym_label,
    STATE(74), 2,
      sym_variable_symbol,
      sym_sequence_symbol,
    STATE(17), 7,
      sym__line,
      sym_blank_line,
      sym_comment_line,
      sym_macro_comment_line,
      sym_process_statement,
      sym_instruction_statement,
      aux_sym_source_file_repeat1,
  [534] = 12,
    ACTIONS(51), 1,
      ts_builtin_sym_end,
    ACTIONS(53), 1,
      anon_sym_LF,
    ACTIONS(56), 1,
      anon_sym_STAR,
    ACTIONS(59), 1,
      aux_sym_macro_comment_line_token1,
    ACTIONS(62), 1,
      aux_sym_process_statement_token1,
    ACTIONS(65), 1,
      sym_symbol,
    ACTIONS(68), 1,
      anon_sym_AMP,
    ACTIONS(71), 1,
      anon_sym_DOT,
    STATE(2), 1,
      sym_operation,
    STATE(71), 1,
      sym_label,
    STATE(74), 2,
      sym_variable_symbol,
      sym_sequence_symbol,
    STATE(17), 7,
      sym__line,
      sym_blank_line,
      sym_comment_line,
      sym_macro_comment_line,
      sym_process_statement,
      sym_instruction_statement,
      aux_sym_source_file_repeat1,
  [578] = 3,
    ACTIONS(78), 1,
      sym_symbol,
    ACTIONS(76), 7,
      sym_dc_type_spec,
      sym_number,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
      aux_sym_attribute_reference_token1,
    ACTIONS(74), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_LPAREN,
      aux_sym_dc_value_token1,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_AMP,
      anon_sym_EQ,
  [601] = 2,
    ACTIONS(74), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_LPAREN,
      aux_sym_dc_value_token1,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_AMP,
      anon_sym_EQ,
    ACTIONS(76), 8,
      sym_dc_type_spec,
      sym_symbol,
      sym_number,
      sym_hex_self_defining_term,
      sym_binary_self_defining_term,
      sym_character_self_defining_term,
      sym_graphic_self_defining_term,
      aux_sym_attribute_reference_token1,
  [622] = 1,
    ACTIONS(81), 9,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
      sym_symbol,
  [634] = 2,
    ACTIONS(85), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(83), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [647] = 2,
    ACTIONS(89), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(87), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [660] = 2,
    ACTIONS(93), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(91), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [673] = 1,
    ACTIONS(95), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [684] = 2,
    ACTIONS(99), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(97), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [697] = 2,
    ACTIONS(103), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(101), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [710] = 1,
    ACTIONS(105), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [721] = 2,
    ACTIONS(109), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(107), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [734] = 2,
    ACTIONS(113), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(111), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [747] = 1,
    ACTIONS(115), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [758] = 2,
    ACTIONS(119), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(117), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [771] = 1,
    ACTIONS(121), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [782] = 2,
    ACTIONS(125), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(123), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [795] = 2,
    ACTIONS(129), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(127), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [808] = 1,
    ACTIONS(131), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [819] = 2,
    ACTIONS(135), 2,
      anon_sym_STAR,
      anon_sym_DOT,
    ACTIONS(133), 6,
      ts_builtin_sym_end,
      anon_sym_LF,
      aux_sym_macro_comment_line_token1,
      aux_sym_process_statement_token1,
      sym_symbol,
      anon_sym_AMP,
  [832] = 1,
    ACTIONS(137), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [843] = 1,
    ACTIONS(139), 8,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_LPAREN,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [854] = 2,
    ACTIONS(143), 1,
      anon_sym_LPAREN,
    ACTIONS(141), 6,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [866] = 1,
    ACTIONS(141), 7,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [876] = 5,
    ACTIONS(147), 1,
      anon_sym_COMMA,
    ACTIONS(149), 1,
      anon_sym_RPAREN,
    STATE(56), 1,
      aux_sym_dc_value_repeat1,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [894] = 2,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(153), 5,
      anon_sym_LF,
      anon_sym_COMMA,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
  [906] = 1,
    ACTIONS(153), 7,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [916] = 1,
    ACTIONS(155), 7,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [926] = 1,
    ACTIONS(157), 7,
      anon_sym_LF,
      anon_sym_STAR,
      anon_sym_COMMA,
      anon_sym_RPAREN,
      anon_sym_PLUS,
      anon_sym_DASH,
      anon_sym_SLASH,
  [936] = 3,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(159), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [949] = 4,
    ACTIONS(161), 1,
      anon_sym_COMMA,
    ACTIONS(163), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [964] = 3,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
    ACTIONS(165), 2,
      anon_sym_COMMA,
      anon_sym_RPAREN,
  [977] = 4,
    ACTIONS(41), 1,
      anon_sym_COMMA,
    ACTIONS(167), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [992] = 3,
    ACTIONS(163), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [1004] = 4,
    ACTIONS(15), 1,
      anon_sym_AMP,
    ACTIONS(21), 1,
      anon_sym_STAR,
    ACTIONS(169), 1,
      sym_symbol,
    STATE(37), 2,
      sym_variable_symbol,
      sym_location_counter,
  [1018] = 3,
    ACTIONS(171), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [1030] = 3,
    ACTIONS(173), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [1042] = 4,
    ACTIONS(177), 1,
      anon_sym_LPAREN,
    ACTIONS(179), 1,
      aux_sym_dc_value_token1,
    STATE(70), 1,
      sym_dc_value,
    ACTIONS(175), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1056] = 3,
    ACTIONS(167), 1,
      anon_sym_RPAREN,
    ACTIONS(145), 2,
      anon_sym_STAR,
      anon_sym_SLASH,
    ACTIONS(151), 2,
      anon_sym_PLUS,
      anon_sym_DASH,
  [1068] = 3,
    ACTIONS(147), 1,
      anon_sym_COMMA,
    ACTIONS(181), 1,
      anon_sym_RPAREN,
    STATE(62), 1,
      aux_sym_dc_value_repeat1,
  [1078] = 3,
    ACTIONS(183), 1,
      anon_sym_LF,
    ACTIONS(185), 1,
      anon_sym_COMMA,
    STATE(57), 1,
      aux_sym_operands_repeat1,
  [1088] = 3,
    ACTIONS(177), 1,
      anon_sym_LPAREN,
    ACTIONS(179), 1,
      aux_sym_dc_value_token1,
    STATE(38), 1,
      sym_dc_value,
  [1098] = 3,
    ACTIONS(188), 1,
      anon_sym_LF,
    ACTIONS(190), 1,
      aux_sym_comment_text_token1,
    STATE(78), 1,
      sym_comment_text,
  [1108] = 3,
    ACTIONS(192), 1,
      anon_sym_LF,
    ACTIONS(194), 1,
      anon_sym_COMMA,
    STATE(61), 1,
      aux_sym_operands_repeat1,
  [1118] = 3,
    ACTIONS(194), 1,
      anon_sym_COMMA,
    ACTIONS(196), 1,
      anon_sym_LF,
    STATE(57), 1,
      aux_sym_operands_repeat1,
  [1128] = 3,
    ACTIONS(165), 1,
      anon_sym_RPAREN,
    ACTIONS(198), 1,
      anon_sym_COMMA,
    STATE(62), 1,
      aux_sym_dc_value_repeat1,
  [1138] = 3,
    ACTIONS(190), 1,
      aux_sym_comment_text_token1,
    ACTIONS(201), 1,
      anon_sym_LF,
    STATE(80), 1,
      sym_comment_text,
  [1148] = 3,
    ACTIONS(203), 1,
      anon_sym_LF,
    ACTIONS(205), 1,
      aux_sym_comment_text_token1,
    STATE(82), 1,
      sym_process_operands,
  [1158] = 1,
    ACTIONS(207), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1163] = 1,
    ACTIONS(209), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1168] = 1,
    ACTIONS(211), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1173] = 1,
    ACTIONS(213), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1178] = 1,
    ACTIONS(183), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1183] = 1,
    ACTIONS(215), 2,
      anon_sym_LF,
      anon_sym_COMMA,
  [1188] = 2,
    ACTIONS(217), 1,
      sym_symbol,
    STATE(3), 1,
      sym_operation,
  [1195] = 1,
    ACTIONS(219), 1,
      anon_sym_SQUOTE,
  [1199] = 1,
    ACTIONS(221), 1,
      anon_sym_LF,
  [1203] = 1,
    ACTIONS(223), 1,
      sym_symbol,
  [1207] = 1,
    ACTIONS(225), 1,
      anon_sym_LF,
  [1211] = 1,
    ACTIONS(227), 1,
      ts_builtin_sym_end,
  [1215] = 1,
    ACTIONS(229), 1,
      aux_sym_variable_symbol_token1,
  [1219] = 1,
    ACTIONS(231), 1,
      anon_sym_LF,
  [1223] = 1,
    ACTIONS(233), 1,
      aux_sym_variable_symbol_token1,
  [1227] = 1,
    ACTIONS(235), 1,
      anon_sym_LF,
  [1231] = 1,
    ACTIONS(237), 1,
      anon_sym_LF,
  [1235] = 1,
    ACTIONS(239), 1,
      anon_sym_LF,
  [1239] = 1,
    ACTIONS(241), 1,
      sym_symbol,
  [1243] = 1,
    ACTIONS(243), 1,
      sym_dc_type_spec,
  [1247] = 1,
    ACTIONS(245), 1,
      anon_sym_LF,
};

static const uint32_t ts_small_parse_table_map[] = {
  [SMALL_STATE(5)] = 0,
  [SMALL_STATE(6)] = 47,
  [SMALL_STATE(7)] = 94,
  [SMALL_STATE(8)] = 138,
  [SMALL_STATE(9)] = 182,
  [SMALL_STATE(10)] = 226,
  [SMALL_STATE(11)] = 270,
  [SMALL_STATE(12)] = 314,
  [SMALL_STATE(13)] = 358,
  [SMALL_STATE(14)] = 402,
  [SMALL_STATE(15)] = 446,
  [SMALL_STATE(16)] = 490,
  [SMALL_STATE(17)] = 534,
  [SMALL_STATE(18)] = 578,
  [SMALL_STATE(19)] = 601,
  [SMALL_STATE(20)] = 622,
  [SMALL_STATE(21)] = 634,
  [SMALL_STATE(22)] = 647,
  [SMALL_STATE(23)] = 660,
  [SMALL_STATE(24)] = 673,
  [SMALL_STATE(25)] = 684,
  [SMALL_STATE(26)] = 697,
  [SMALL_STATE(27)] = 710,
  [SMALL_STATE(28)] = 721,
  [SMALL_STATE(29)] = 734,
  [SMALL_STATE(30)] = 747,
  [SMALL_STATE(31)] = 758,
  [SMALL_STATE(32)] = 771,
  [SMALL_STATE(33)] = 782,
  [SMALL_STATE(34)] = 795,
  [SMALL_STATE(35)] = 808,
  [SMALL_STATE(36)] = 819,
  [SMALL_STATE(37)] = 832,
  [SMALL_STATE(38)] = 843,
  [SMALL_STATE(39)] = 854,
  [SMALL_STATE(40)] = 866,
  [SMALL_STATE(41)] = 876,
  [SMALL_STATE(42)] = 894,
  [SMALL_STATE(43)] = 906,
  [SMALL_STATE(44)] = 916,
  [SMALL_STATE(45)] = 926,
  [SMALL_STATE(46)] = 936,
  [SMALL_STATE(47)] = 949,
  [SMALL_STATE(48)] = 964,
  [SMALL_STATE(49)] = 977,
  [SMALL_STATE(50)] = 992,
  [SMALL_STATE(51)] = 1004,
  [SMALL_STATE(52)] = 1018,
  [SMALL_STATE(53)] = 1030,
  [SMALL_STATE(54)] = 1042,
  [SMALL_STATE(55)] = 1056,
  [SMALL_STATE(56)] = 1068,
  [SMALL_STATE(57)] = 1078,
  [SMALL_STATE(58)] = 1088,
  [SMALL_STATE(59)] = 1098,
  [SMALL_STATE(60)] = 1108,
  [SMALL_STATE(61)] = 1118,
  [SMALL_STATE(62)] = 1128,
  [SMALL_STATE(63)] = 1138,
  [SMALL_STATE(64)] = 1148,
  [SMALL_STATE(65)] = 1158,
  [SMALL_STATE(66)] = 1163,
  [SMALL_STATE(67)] = 1168,
  [SMALL_STATE(68)] = 1173,
  [SMALL_STATE(69)] = 1178,
  [SMALL_STATE(70)] = 1183,
  [SMALL_STATE(71)] = 1188,
  [SMALL_STATE(72)] = 1195,
  [SMALL_STATE(73)] = 1199,
  [SMALL_STATE(74)] = 1203,
  [SMALL_STATE(75)] = 1207,
  [SMALL_STATE(76)] = 1211,
  [SMALL_STATE(77)] = 1215,
  [SMALL_STATE(78)] = 1219,
  [SMALL_STATE(79)] = 1223,
  [SMALL_STATE(80)] = 1227,
  [SMALL_STATE(81)] = 1231,
  [SMALL_STATE(82)] = 1235,
  [SMALL_STATE(83)] = 1239,
  [SMALL_STATE(84)] = 1243,
  [SMALL_STATE(85)] = 1247,
};

static const TSParseActionEntry ts_parse_actions[] = {
  [0] = {.entry = {.count = 0, .reusable = false}},
  [1] = {.entry = {.count = 1, .reusable = false}}, RECOVER(),
  [3] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_source_file, 0, 0, 0),
  [5] = {.entry = {.count = 1, .reusable = true}}, SHIFT(25),
  [7] = {.entry = {.count = 1, .reusable = false}}, SHIFT(59),
  [9] = {.entry = {.count = 1, .reusable = true}}, SHIFT(63),
  [11] = {.entry = {.count = 1, .reusable = true}}, SHIFT(64),
  [13] = {.entry = {.count = 1, .reusable = true}}, SHIFT(18),
  [15] = {.entry = {.count = 1, .reusable = true}}, SHIFT(79),
  [17] = {.entry = {.count = 1, .reusable = false}}, SHIFT(77),
  [19] = {.entry = {.count = 1, .reusable = true}}, SHIFT(26),
  [21] = {.entry = {.count = 1, .reusable = true}}, SHIFT(27),
  [23] = {.entry = {.count = 1, .reusable = true}}, SHIFT(6),
  [25] = {.entry = {.count = 1, .reusable = false}}, SHIFT(54),
  [27] = {.entry = {.count = 1, .reusable = true}}, SHIFT(65),
  [29] = {.entry = {.count = 1, .reusable = true}}, SHIFT(14),
  [31] = {.entry = {.count = 1, .reusable = false}}, SHIFT(39),
  [33] = {.entry = {.count = 1, .reusable = false}}, SHIFT(32),
  [35] = {.entry = {.count = 1, .reusable = true}}, SHIFT(84),
  [37] = {.entry = {.count = 1, .reusable = false}}, SHIFT(72),
  [39] = {.entry = {.count = 1, .reusable = true}}, SHIFT(33),
  [41] = {.entry = {.count = 1, .reusable = true}}, SHIFT(15),
  [43] = {.entry = {.count = 1, .reusable = true}}, SHIFT(8),
  [45] = {.entry = {.count = 1, .reusable = true}}, SHIFT(40),
  [47] = {.entry = {.count = 1, .reusable = true}}, SHIFT(9),
  [49] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_source_file, 1, 0, 0),
  [51] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0),
  [53] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(25),
  [56] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(59),
  [59] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(63),
  [62] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(64),
  [65] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(18),
  [68] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(79),
  [71] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_source_file_repeat1, 2, 0, 0), SHIFT_REPEAT(77),
  [74] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_operation, 1, 0, 0),
  [76] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_operation, 1, 0, 0),
  [78] = {.entry = {.count = 2, .reusable = false}}, REDUCE(sym_label, 1, 0, 0), REDUCE(sym_operation, 1, 0, 0),
  [81] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_variable_symbol, 2, 0, 0),
  [83] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_comment_line, 3, 0, 0),
  [85] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_comment_line, 3, 0, 0),
  [87] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_instruction_statement, 3, 0, 5),
  [89] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_instruction_statement, 3, 0, 5),
  [91] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_process_statement, 2, 0, 0),
  [93] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_process_statement, 2, 0, 0),
  [95] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_dc_value, 1, 0, 0),
  [97] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_blank_line, 1, 0, 0),
  [99] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_blank_line, 1, 0, 0),
  [101] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_instruction_statement, 2, 0, 1),
  [103] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_instruction_statement, 2, 0, 1),
  [105] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_location_counter, 1, 0, 0),
  [107] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_comment_line, 2, 0, 0),
  [109] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_comment_line, 2, 0, 0),
  [111] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_instruction_statement, 4, 0, 6),
  [113] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_instruction_statement, 4, 0, 6),
  [115] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_dc_value, 4, 0, 0),
  [117] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_macro_comment_line, 2, 0, 0),
  [119] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_macro_comment_line, 2, 0, 0),
  [121] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_self_defining_term, 1, 0, 0),
  [123] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_instruction_statement, 3, 0, 3),
  [125] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_instruction_statement, 3, 0, 3),
  [127] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_process_statement, 3, 0, 0),
  [129] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_process_statement, 3, 0, 0),
  [131] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_dc_value, 3, 0, 0),
  [133] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_macro_comment_line, 3, 0, 0),
  [135] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_macro_comment_line, 3, 0, 0),
  [137] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_attribute_reference, 3, 0, 0),
  [139] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_literal, 3, 0, 0),
  [141] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_expression, 1, 0, 0),
  [143] = {.entry = {.count = 1, .reusable = true}}, SHIFT(5),
  [145] = {.entry = {.count = 1, .reusable = true}}, SHIFT(11),
  [147] = {.entry = {.count = 1, .reusable = true}}, SHIFT(12),
  [149] = {.entry = {.count = 1, .reusable = true}}, SHIFT(35),
  [151] = {.entry = {.count = 1, .reusable = true}}, SHIFT(13),
  [153] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_binary_expression, 3, 0, 0),
  [155] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_unary_expression, 2, 0, 0),
  [157] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_parenthesized_expression, 3, 0, 0),
  [159] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym__operand, 1, 0, 0),
  [161] = {.entry = {.count = 1, .reusable = true}}, SHIFT(10),
  [163] = {.entry = {.count = 1, .reusable = true}}, SHIFT(67),
  [165] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_dc_value_repeat1, 2, 0, 0),
  [167] = {.entry = {.count = 1, .reusable = true}}, SHIFT(45),
  [169] = {.entry = {.count = 1, .reusable = true}}, SHIFT(37),
  [171] = {.entry = {.count = 1, .reusable = true}}, SHIFT(68),
  [173] = {.entry = {.count = 1, .reusable = true}}, SHIFT(66),
  [175] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_dc_operand, 1, 0, 2),
  [177] = {.entry = {.count = 1, .reusable = true}}, SHIFT(7),
  [179] = {.entry = {.count = 1, .reusable = true}}, SHIFT(24),
  [181] = {.entry = {.count = 1, .reusable = true}}, SHIFT(30),
  [183] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_operands_repeat1, 2, 0, 0),
  [185] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_operands_repeat1, 2, 0, 0), SHIFT_REPEAT(4),
  [188] = {.entry = {.count = 1, .reusable = false}}, SHIFT(28),
  [190] = {.entry = {.count = 1, .reusable = true}}, SHIFT(73),
  [192] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_operands, 1, 0, 0),
  [194] = {.entry = {.count = 1, .reusable = true}}, SHIFT(4),
  [196] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_operands, 2, 0, 0),
  [198] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_dc_value_repeat1, 2, 0, 0), SHIFT_REPEAT(12),
  [201] = {.entry = {.count = 1, .reusable = false}}, SHIFT(31),
  [203] = {.entry = {.count = 1, .reusable = false}}, SHIFT(23),
  [205] = {.entry = {.count = 1, .reusable = true}}, SHIFT(81),
  [207] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_string_literal, 1, 0, 0),
  [209] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_address_operand, 5, 0, 0),
  [211] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_address_operand, 4, 0, 0),
  [213] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_address_operand, 6, 0, 0),
  [215] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_dc_operand, 2, 0, 4),
  [217] = {.entry = {.count = 1, .reusable = true}}, SHIFT(19),
  [219] = {.entry = {.count = 1, .reusable = true}}, SHIFT(51),
  [221] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_comment_text, 1, 0, 0),
  [223] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_label, 1, 0, 0),
  [225] = {.entry = {.count = 1, .reusable = true}}, SHIFT(29),
  [227] = {.entry = {.count = 1, .reusable = true}},  ACCEPT_INPUT(),
  [229] = {.entry = {.count = 1, .reusable = true}}, SHIFT(83),
  [231] = {.entry = {.count = 1, .reusable = true}}, SHIFT(21),
  [233] = {.entry = {.count = 1, .reusable = true}}, SHIFT(20),
  [235] = {.entry = {.count = 1, .reusable = true}}, SHIFT(36),
  [237] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_process_operands, 1, 0, 0),
  [239] = {.entry = {.count = 1, .reusable = true}}, SHIFT(34),
  [241] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_sequence_symbol, 2, 0, 0),
  [243] = {.entry = {.count = 1, .reusable = true}}, SHIFT(58),
  [245] = {.entry = {.count = 1, .reusable = true}}, SHIFT(22),
};

#ifdef __cplusplus
extern "C" {
#endif
#ifdef TREE_SITTER_HIDE_SYMBOLS
#define TS_PUBLIC
#elif defined(_WIN32)
#define TS_PUBLIC __declspec(dllexport)
#else
#define TS_PUBLIC __attribute__((visibility("default")))
#endif

TS_PUBLIC const TSLanguage *tree_sitter_hlasm(void) {
  static const TSLanguage language = {
    .version = LANGUAGE_VERSION,
    .symbol_count = SYMBOL_COUNT,
    .alias_count = ALIAS_COUNT,
    .token_count = TOKEN_COUNT,
    .external_token_count = EXTERNAL_TOKEN_COUNT,
    .state_count = STATE_COUNT,
    .large_state_count = LARGE_STATE_COUNT,
    .production_id_count = PRODUCTION_ID_COUNT,
    .field_count = FIELD_COUNT,
    .max_alias_sequence_length = MAX_ALIAS_SEQUENCE_LENGTH,
    .parse_table = &ts_parse_table[0][0],
    .small_parse_table = ts_small_parse_table,
    .small_parse_table_map = ts_small_parse_table_map,
    .parse_actions = ts_parse_actions,
    .symbol_names = ts_symbol_names,
    .field_names = ts_field_names,
    .field_map_slices = ts_field_map_slices,
    .field_map_entries = ts_field_map_entries,
    .symbol_metadata = ts_symbol_metadata,
    .public_symbol_map = ts_symbol_map,
    .alias_map = ts_non_terminal_alias_map,
    .alias_sequences = &ts_alias_sequences[0][0],
    .lex_modes = ts_lex_modes,
    .lex_fn = ts_lex,
    .primary_state_ids = ts_primary_state_ids,
  };
  return &language;
}
#ifdef __cplusplus
}
#endif
