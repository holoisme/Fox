package holo.lang.parser;

import holo.errors.FoxError;
import holo.interpreter.nodes.Node;

public record ParseResult(Node node, FoxError error) {}