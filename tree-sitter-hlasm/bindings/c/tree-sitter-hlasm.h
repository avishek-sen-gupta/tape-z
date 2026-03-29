#ifndef TREE_SITTER_HLASM_H_
#define TREE_SITTER_HLASM_H_

typedef struct TSLanguage TSLanguage;

#ifdef __cplusplus
extern "C" {
#endif

const TSLanguage *tree_sitter_hlasm(void);

#ifdef __cplusplus
}
#endif

#endif // TREE_SITTER_HLASM_H_
