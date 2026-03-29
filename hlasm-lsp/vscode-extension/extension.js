const { workspace } = require("vscode");
const {
  LanguageClient,
  TransportKind,
} = require("vscode-languageclient/node");

let client;

function activate(context) {
  const pythonPath =
    workspace.getConfiguration("hlasmLsp").get("pythonPath") || "python3";

  const serverOptions = {
    command: pythonPath,
    args: ["-m", "hlasm_lsp"],
    transport: TransportKind.stdio,
  };

  const clientOptions = {
    documentSelector: [{ scheme: "file", language: "hlasm" }],
  };

  client = new LanguageClient(
    "hlasm-lsp",
    "HLASM Language Server",
    serverOptions,
    clientOptions
  );

  client.start();
}

function deactivate() {
  if (client) {
    return client.stop();
  }
}

module.exports = { activate, deactivate };
