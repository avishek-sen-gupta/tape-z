from hlasm_lsp.mnemonics import load_mnemonics, MnemonicInfo, ASSEMBLER_DIRECTIVES


def test_load_mnemonics_returns_dict():
    mnemonics = load_mnemonics()
    assert isinstance(mnemonics, dict)
    assert len(mnemonics) > 100


def test_mnemonic_has_expected_fields():
    mnemonics = load_mnemonics()
    info = mnemonics["LR"]
    assert info.mnemonic == "LR"
    assert info.format == "RR"
    assert len(info.description) > 0
    assert len(info.operands) > 0


def test_mnemonic_lookup_is_case_insensitive():
    mnemonics = load_mnemonics()
    assert "LR" in mnemonics
    assert "A" in mnemonics
    assert "MVC" in mnemonics


def test_assembler_directives_present():
    assert "CSECT" in ASSEMBLER_DIRECTIVES
    assert "DSECT" in ASSEMBLER_DIRECTIVES
    assert "USING" in ASSEMBLER_DIRECTIVES
    assert "EQU" in ASSEMBLER_DIRECTIVES
    assert "DC" in ASSEMBLER_DIRECTIVES
    assert "DS" in ASSEMBLER_DIRECTIVES
    assert "END" in ASSEMBLER_DIRECTIVES


def test_dc_ds_type_descriptions():
    from hlasm_lsp.mnemonics import DC_TYPE_DESCRIPTIONS

    assert "F" in DC_TYPE_DESCRIPTIONS
    assert "fullword" in DC_TYPE_DESCRIPTIONS["F"].lower()
    assert "H" in DC_TYPE_DESCRIPTIONS
    assert "X" in DC_TYPE_DESCRIPTIONS
    assert "C" in DC_TYPE_DESCRIPTIONS
