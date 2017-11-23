// Generated from Arithmetic.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArithmeticParser}.
 */
public interface ArithmeticListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(ArithmeticParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(ArithmeticParser.EquationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(ArithmeticParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(ArithmeticParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#relop}.
	 * @param ctx the parse tree
	 */
	void enterRelop(ArithmeticParser.RelopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#relop}.
	 * @param ctx the parse tree
	 */
	void exitRelop(ArithmeticParser.RelopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(ArithmeticParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(ArithmeticParser.OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(ArithmeticParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(ArithmeticParser.NumberContext ctx);
}