package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record ExtractFromObjectInstance(String name, boolean obligatory, Sequence sequence) implements Sequenced {

}
