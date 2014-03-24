/**
 * Test Core Reverse Link Graph code
 */
package AshleyIngram.FYP.Core.Tests

import AshleyIngram.FYP.Core.ReverseLinkGraph
import org.scalatest._

class ReverseLinkGraphTests extends FlatSpec with Matchers {
  it should "return an empty list if there's no a tags" in {
    val text = "Hello World"
    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (0)
  }

  it should "return correct address with basic link" in {
    val text = "<a href=\"foo.htm\">"
    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (1)
    results(0) should be ("foo.htm")
  }

  it should "return correct address with link with other attributes" in {
    val text = """<a href="foo.htm" class="bar">"""
    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (1)
    results(0) should be ("foo.htm")
  }

  it should "return correct address in nested html fragment" in {
    val text = """<div class="container"><a href="foo.htm">Hello World!</a></div>"""
    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (1)
    results(0) should be ("foo.htm")
  }

  it should "return more than one match for multiple links" in {
    val text =
      """<div class="container">
        | <a href="foo.htm">Hello World!</a>
        | <a href="bar.htm">Hello World!</a>
        | </div>"""

    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (2)
    results(0) should be ("foo.htm")
    results(1) should be ("bar.htm")
  }

  it should "parse an actual file from the data" in {
    val text = """<!DOCTYPE html>
                 |<html class="client-nojs" dir="ltr" lang="en">
                 | <head>
                 |  <title>Programming language</title>
                 |  <meta charset="UTF-8" />
                 |
                 |
                 |  <link href="../../disclaimer.htm" rel="copyright" />
                 |  <link rel="stylesheet" href="../../css/en-shared.css" />
                 |  <!--[if IE 6]><link rel="stylesheet" href="../../css/IE60Fixes.css" media="screen" /><![endif]-->
                 |  <!--[if IE 7]><link rel="stylesheet" href="../../css/IE70Fixes.css" media="screen" /><![endif]-->
                 |  <meta content="" name="ResourceLoaderDynamicStyles" />
                 |  <link rel="stylesheet" href="../../css/en-site-vector.css" />
                 |  <style>a:lang(ar),a:lang(ckb),a:lang(fa),a:lang(kk-arab),a:lang(mzn),a:lang(ps),a:lang(ur){text-decoration:none}.editsection{display:none}
                 |/* cache key: enwiki:resourceloader:filter:minify-css:7:79e96594f03002bbea98f74c22274594 */</style>
                 |  <link href="http://schools-wikipedia.org/wp/p/Programming_language.htm" rel="canonical" />
                 |  <link href="http://www.soschildrensvillages.org.uk/wikipedia-for-schools-license" rel="license" />
                 |  <meta content="An article about Programming language hand selected for the Wikipedia for Schools by SOS Children" name="description" />
                 | </head>
                 | <body class="mediawiki ltr sitedir-ltr ns-0 ns-subject page-Programming_language skin-monobook action-view">
                 |  <div id="globalWrapper">
                 |   <div id="column-content">
                 |    <div class="mw-body-primary" id="content" role="main"><a id="top"></a><a id="badge" href="../../disclaimer.htm"><img alt="Checked content" src="../../checked-content.png" width="150" height="70" /></a><h1 class="firstHeading" id="firstHeading" lang="en"><span dir="auto">Programming language</span></h1>
                 |     <div class="mw-body" id="bodyContent">
                 |      <div id="siteSub">Related subjects: <a href="../index/subject.IT.Computer_Programming.htm">Computer Programming</a></div>
                 |      <!-- start content -->
                 |      <div class="mw-content-ltr" dir="ltr" id="mw-content-text" lang="en"><div class="thumb tleft" id="sitebackground"><div style="width:152px;" class="thumbinner"><h4>About this schools Wikipedia selection</h4><p>SOS believes education gives a better chance in life to children in the developing world too. A good way to help other children is by <a href="http://www.sponsor-a-child.org.uk/">sponsoring a child</a></p></div></div>
                 |       <p>A <b>programming language</b> is an <!--del_lnk--> artificial language that can be used to control the behaviour of a machine, particularly a <a href="../../wp/c/Computer.htm" title="Computer">computer</a>. Programming languages are defined by <!--del_lnk--> syntactic and <!--del_lnk--> semantic rules which describe their structure and meaning respectively. Many programming languages have some form of written specification of their syntax and semantics; some are defined by an official implementation (eg, an <!--del_lnk--> ISO Standard), while others have a dominant implementation (eg, <a href="../../wp/p/Perl.htm" title="Perl">Perl</a>).</p>
                 |       <p>Programming languages are also used to facilitate communication about the task of organizing and manipulating information, and to express <a href="../../wp/a/Algorithm.htm" title="Algorithm">algorithms</a> precisely. Some authors restrict the term "programming language" to those languages that can express <i>all</i> possible algorithms; sometimes the term "<a class="mw-redirect" href="../../wp/p/Programming_language.htm" title="Computer language">computer language</a>" is used for more limited artificial languages.</p>
                 |       <table cellpadding="0" cellspacing="5" class="vertical-navbox nowraplinks" style="float:right;clear:right;width:22.0em;margin:0 0 1.0em 1.0em;background:#f9f9f9;border:1px solid #aaa;padding:0.2em;border-spacing:0.4em 0;text-align:center;line-height:1.4em;font-size:88%;background:#f9f9f9; width:auto">
                 |        <tr>
                 |         <th class="" style="padding:0.1em;;background:#ccccff; font-size:110%;"><strong class="selflink">Programming language</strong><br /> lists</th>
                 |        </tr>
                 |        <tr>
                 |         <td class="" style="padding:0 0.1em 0.4em; text-align: left; font-size: 110% Subculture;">
                 |          <ul>
                 |           <li><!--del_lnk--> Alphabetical</li>
                 |           <li><!--del_lnk--> Categorical</li>
                 |           <li><!--del_lnk--> Chronological</li>
                 |           <li><!--del_lnk--> Generational</li>
                 |          </ul>
                 |         </td>
                 |        </tr>
                 |        <tr>
                 |         <td style="text-align:right;font-size:115%;">
                 |         </td>
                 |        </tr>
                 |       </table>
                 |       <p>Thousands of different programming languages have been created so far, and new languages are created every year.</p>
                 |       <h2><span class="mw-headline" id="Definitions">Definitions</span></h2>
                 |       <p>Traits often considered important for constituting a programming language:</p>
                 |       <ul>
                 |        <li><i>Function:</i> A programming language is a language used to write <!--del_lnk--> computer programs, which involve a <a href="../../wp/c/Computer.htm" title="Computer">computer</a> performing some kind of <!--del_lnk--> computation or <a href="../../wp/a/Algorithm.htm" title="Algorithm">algorithm</a> and possibly control external devices such as <!--del_lnk--> printers, robots, and so on.</li>
                 |       </ul>
                 |       <ul>
                 |        <li><i>Target:</i> Programming languages differ from <!--del_lnk--> natural languages in that natural languages are only used for interaction between people, while programming languages also allow humans to communicate instructions to machines. Some programming languages are used by one device to control another. For example <!--del_lnk--> PostScript programs are frequently created by another program to control a <!--del_lnk--> computer printer or display.</li>
                 |       </ul>
                 |       <ul>
                 |        <li><i>Constructs:</i> Programming languages may contain constructs for defining and manipulating <!--del_lnk--> data structures or controlling the <!--del_lnk--> flow of execution.</li>
                 |       </ul>
                 |       <ul>
                 |        <li><i>Expressive power:</i> The <!--del_lnk--> theory of computation classifies languages by the computations they are capable of expressing. All <!--del_lnk--> Turing complete languages can implement the same set of <a href="../../wp/a/Algorithm.htm" title="Algorithm">algorithms</a>. <!--del_lnk--> ANSI/ISO SQL and <!--del_lnk--> Charity are examples of languages that are not Turing complete yet often called programming languages.</li>
                 |       </ul>
                 |       <p>Non-computational languages, such as <a href="../../wp/m/Markup_language.htm" title="Markup language">markup languages</a> like <!--del_lnk--> HTML or <!--del_lnk--> formal grammars like <!--del_lnk--> BNF, are usually not considered programming languages. A programming language (which may or may not be Turing complete) may be embedded in these non-computational (host) languages.</p>
                 |       <h2><span class="mw-headline" id="Purpose">Purpose</span></h2>
                 |       <p>A prominent purpose of programming languages is to provide instructions to a computer. As such, programming languages differ from most other forms of human expression in that they require a greater degree of precision and completeness. When using a natural language to communicate with other people, human authors and speakers can be ambiguous and make small errors, and still expect their intent to be understood. However, figuratively speaking, computers "do exactly what they are told to do", and cannot "understand" what code the programmer intended to write. The combination of the language definition, the <!--del_lnk--> program, and the program's inputs must fully specify the external behaviour that occurs when the program is executed.</p>
                 |       <p>Many languages have been designed from scratch, altered to meet new needs, combined with other languages, and eventually fallen into disuse. Although there have been attempts to design one "universal" computer language that serves all purposes, all of them have failed to be accepted in this role. The need for diverse computer languages arises from the diversity of contexts in which languages are used:</p>
                 |       <ul>
                 |        <li>Programs range from tiny scripts written by individual hobbyists to huge systems written by hundreds of programmers.</li>
                 |        <li>Programmers range in expertise from novices who need simplicity above all else, to experts who may be comfortable with considerable complexity.</li>
                 |        <li>Programs must balance speed, size, and simplicity on systems ranging from <!--del_lnk--> microcontrollers to <!--del_lnk--> supercomputers.</li>
                 |        <li>Programs may be written once and not change for generations, or they may undergo nearly constant modification.</li>
                 |        <li>Finally, programmers may simply differ in their tastes: they may be accustomed to discussing problems and expressing them in a particular language.</li>
                 |       </ul>
                 |       <p>One common trend in the development of programming languages has been to add more ability to solve problems using a higher level of <!--del_lnk--> abstraction. The earliest programming languages were tied very closely to the underlying hardware of the computer. As new programming languages have developed, features have been added that let programmers express ideas that are more remote from simple translation into underlying hardware instructions. Because programmers are less tied to the complexity of the computer, their programs can do more computing with less effort from the programmer. This lets them write more functionality per time unit.</p>
                 |       <p><!--del_lnk--> Natural language processors have been proposed as a way to eliminate the need for a specialized language for programming. However, this goal remains distant and its benefits are open to debate. <!--del_lnk--> Edsger Dijkstra took the position that the use of a formal language is essential to prevent the introduction of meaningless constructs, and dismissed natural language programming as "foolish." <!--del_lnk--> Alan Perlis was similarly dismissive of the idea.</p>
                 |       <h2><span class="mw-headline" id="Elements">Elements</span></h2>
                 |       <h3><span class="mw-headline" id="Syntax">Syntax</span></h3>
                 |       <div class="thumb tright">
                 |        <div class="thumbinner" style="width:398px;"><a class="image" href="../../images/592/59260.png.htm"><img alt="" class="thumbimage" height="304" src="../../images/592/59260.png"  width="396" /></a><div class="thumbcaption">
                 |          <div class="magnify"><a class="internal" href="../../images/592/59260.png.htm" title="Enlarge"><img alt="" height="11" src="../../images/1x1white.gif" title="This image is not present because of licensing restrictions" width="15" /></a></div><!--del_lnk--> Parse tree of Python code with inset tokenization</div>
                 |        </div>
                 |       </div>
                 |       <div class="thumb tright">
                 |        <div class="thumbinner" style="width:294px;"><a class="image" href="../../images/592/59264.png.htm"><img alt="" class="thumbimage" height="218" src="../../images/592/59264.png"  width="292" /></a><div class="thumbcaption">
                 |          <div class="magnify"><a class="internal" href="../../images/592/59264.png.htm" title="Enlarge"><img alt="" height="11" src="../../images/1x1white.gif" title="This image is not present because of licensing restrictions" width="15" /></a></div><!--del_lnk--> Syntax highlighting is often used to aid programmers in recognizing elements of source code. The language above is <a href="../../wp/p/Python_%2528programming_language%2529.htm" title="Python (programming language)">Python</a>.</div>
                 |        </div>
                 |       </div>
                 |       <p>A programming language's surface form is known as its <!--del_lnk--> syntax. Most programming languages are purely textual; they use sequences of text including words, numbers, and punctuation, much like written natural languages. On the other hand, there are some programming languages which are more <!--del_lnk--> graphical in nature, using spatial relationships between symbols to specify a program.</p>
                 |       <p>The syntax of a language describes the possible combinations of symbols that form a syntactically correct program. The meaning given to a combination of symbols is handled by semantics (either <!--del_lnk--> formal or hard-coded in a <!--del_lnk--> reference implementation). Since most languages are textual, this article discusses textual syntax.</p>
                 |       <p>Programming language syntax is usually defined using a combination of <!--del_lnk--> regular expressions (for <!--del_lnk--> lexical structure) and <!--del_lnk--> Backus-Naur Form (for <!--del_lnk--> grammatical structure). Below is a simple grammar, based on <!--del_lnk--> Lisp:</p>
                 |       <p><code>expression&nbsp;::= atom | list<br /> atom &nbsp;::= number | symbol<br /> number &nbsp;::= [+-]?['0'-'9']+<br /> symbol &nbsp;::= ['A'-'Z''a'-'z'].*<br /> list &nbsp;::= '(' expression* ')'<br /></code></p>
                 |       <p>This grammar specifies the following:</p>
                 |       <ul>
                 |        <li>an <i>expression</i> is either an <i>atom</i> or a <i>list</i>;</li>
                 |        <li>an <i>atom</i> is either a <i>number</i> or a <i>symbol</i>;</li>
                 |        <li>a <i>number</i> is an unbroken sequence of one or more decimal digits, optionally preceded by a plus or minus sign;</li>
                 |        <li>a <i>symbol</i> is a letter followed by zero or more of any characters (excluding whitespace); and</li>
                 |        <li>a <i>list</i> is a matched pair of parentheses, with zero or more <i>expressions</i> inside it.</li>
                 |       </ul>
                 |       <p>The following are examples of well-formed token sequences in this grammar: '<code>12345</code>', '<code>()</code>', '<code>(a b c232 (1))</code>'</p>
                 |       <p>Not all syntactically correct programs are semantically correct. Many syntactically correct programs are nonetheless ill-formed, per the language's rules; and may (depending on the language specification and the soundness of the implementation) result in an error on translation or execution. In some cases, such programs may exhibit <!--del_lnk--> undefined behaviour. Even when a program is well-defined within a language, it may still have a meaning that is not intended by the person who wrote it.</p>
                 |       <p>Using <!--del_lnk--> natural language as an example, it may not be possible to assign a meaning to a grammatically correct sentence or the sentence may be false:</p>
                 |       <ul>
                 |        <li>"<!--del_lnk--> Colorless green ideas sleep furiously." is grammatically well-formed but has no generally accepted meaning.</li>
                 |        <li>"John is a married bachelor." is grammatically well-formed but expresses a meaning that cannot be true.</li>
                 |       </ul>
                 |       <p>The following C language fragment is syntactically correct, but performs an operation that is not semantically defined (because <tt>p</tt> is a <!--del_lnk--> null pointer, the operations <tt>p->real</tt> and <tt>p->im</tt> have no meaning):</p><pre>
                 |complex *p = NULL;
                 |complex abs_p = sqrt (p->real * p->real + p->im * p->im);
                 |</pre><p>The grammar needed to specify a programming language can be classified by its position in the <!--del_lnk--> Chomsky hierarchy. The syntax of most programming languages can be specified using a Type-2 grammar, i.e., they are <!--del_lnk--> context-free grammars.</p>
                 |       <h3><span class="mw-headline" id="Static_semantics">Static semantics</span></h3>
                 |       <p>The static semantics defines restrictions on the structure of valid texts that are hard or impossible to express in standard syntactic formalisms. The most important of these restrictions are covered by type systems.</p>
                 |       <h3><span class="mw-headline" id="Type_system">Type system</span></h3>
                 |       <p>A type system defines how a programming language classifies values and expressions into <i>types</i>, how it can manipulate those types and how they interact. This generally includes a description of the <!--del_lnk--> data structures that can be constructed in the language. The design and study of type systems using formal mathematics is known as <i><!--del_lnk--> type theory</i>.</p>
                 |       <p>Internally, all <!--del_lnk--> data in modern digital computers are stored simply as zeros or ones (<a class="mw-redirect" href="../../wp/b/Binary_number.htm" title="Binary numeral system">binary</a>).</p>
                 |       <h4><span class="mw-headline" id="Typed_versus_untyped_languages">Typed versus untyped languages</span></h4>
                 |       <p>A language is <i>typed</i> if the specification of every operation defines types of data to which the operation is applicable, with the implication that it is not applicable to other types. For example, "<code>this text between the quotes</code>" is a string. In most programming languages, dividing a number by a string has no meaning. Most modern programming languages will therefore reject any program attempting to perform such an operation. In some languages, the meaningless operation will be detected when the program is compiled ("static" type checking), and rejected by the compiler, while in others, it will be detected when the program is run ("dynamic" type checking), resulting in a runtime <!--del_lnk--> exception.</p>
                 |       <p>A special case of typed languages are the <i>single-type</i> languages. These are often scripting or markup languages, such as <!--del_lnk--> Rexx or <!--del_lnk--> SGML, and have only one data type &mdash; most commonly character strings which are used for both symbolic and numeric data.</p>
                 |       <p>In contrast, an <i>untyped language</i>, such as most <!--del_lnk--> assembly languages, allows any operation to be performed on any data, which are generally considered to be sequences of bits of various lengths. High-level languages which are untyped include <!--del_lnk--> BCPL and some varieties of <!--del_lnk--> Forth.</p>
                 |       <p>In practice, while few languages are considered typed from the point of view of <!--del_lnk--> type theory (verifying or rejecting <i>all</i> operations), most modern languages offer a degree of typing. Many production languages provide means to bypass or subvert the type system.</p>
                 |       <h4><span class="mw-headline" id="Static_versus_dynamic_typing">Static versus dynamic typing</span></h4>
                 |       <p>In <i><!--del_lnk--> static typing</i> all expressions have their types determined prior to the program being run (typically at compile-time). For example, 1 and (2+2) are integer expressions; they cannot be passed to a function that expects a string, or stored in a variable that is defined to hold dates.</p>
                 |       <p>Statically-typed languages can be <i>manifestly typed</i> or <i><!--del_lnk--> type-inferred</i>. In the first case, the programmer must explicitly write types at certain textual positions (for example, at variable <!--del_lnk--> declarations). In the second case, the compiler <i>infers</i> the types of expressions and declarations based on context. Most mainstream statically-typed languages, such as <a class="mw-redirect" href="../../wp/c/C%252B%252B.htm" title="C Plus Plus">C++</a>, <!--del_lnk--> C# and <a href="../../wp/j/Java_%2528programming_language%2529.htm" title="Java (programming language)">Java</a>, are manifestly typed. Complete type inference has traditionally been associated with less mainstream languages, such as <!--del_lnk--> Haskell and <!--del_lnk--> ML. However, many manifestly typed languages support partial type inference; for example, <a href="../../wp/j/Java_%2528programming_language%2529.htm" title="Java (programming language)">Java</a> and <!--del_lnk--> C# both infer types in certain limited cases. <i>Dynamic typing</i>, also called <i>latent typing</i>, determines the type-safety of operations at runtime; in other words, types are associated with <i>runtime values</i> rather than <i>textual expressions</i>. As with type-inferred languages, dynamically typed languages do not require the programmer to write explicit type annotations on expressions. Among other things, this may permit a single variable to refer to values of different types at different points in the program execution. However, type errors cannot be automatically detected until a piece of code is actually executed, making debugging more difficult. <!--del_lnk--> Ruby, <!--del_lnk--> Lisp, <!--del_lnk--> JavaScript, and <a href="../../wp/p/Python_%2528programming_language%2529.htm" title="Python (programming language)">Python</a> are dynamically typed.</p>
                 |       <h4><span class="mw-headline" id="Weak_and_strong_typing">Weak and strong typing</span></h4>
                 |       <p><i>Weak typing</i> allows a value of one type to be treated as another, for example treating a string as a number. This can occasionally be useful, but it can also allow some kinds of program faults to go undetected at <!--del_lnk--> compile time and even at <!--del_lnk--> run time.</p>
                 |       <p><i>Strong typing</i> prevents the above. An attempt to perform an operation on the wrong type of value raises an error. Strongly-typed languages are often termed <i>type-safe</i> or <i><!--del_lnk--> safe</i>.</p>
                 |       <p>An alternative definition for "weakly typed" refers to languages, such as <a href="../../wp/p/Perl.htm" title="Perl">Perl</a>, <!--del_lnk--> JavaScript, and <a href="../../wp/c/C%252B%252B.htm" title="C++">C++</a>, which permit a large number of implicit type conversions. In JavaScript, for example, the expression <code>2 * x</code> implicitly converts <code>x</code> to a number, and this conversion succeeds even if <code>x</code> is <code>null</code>, <code>undefined</code>, an <code>Array</code>, or a string of letters. Such implicit conversions are often useful, but they can mask programming errors.</p>
                 |       <p><i>Strong</i> and <i>static</i> are now generally considered orthogonal concepts, but usage in the literature differs. Some use the term <i>strongly typed</i> to mean <i>strongly, statically typed</i>, or, even more confusingly, to mean simply <i>statically typed</i>. Thus <a href="../../wp/c/C_%2528programming_language%2529.htm" title="C (programming language)">C</a> has been called both strongly typed and weakly, statically typed.</p>
                 |       <h3><span class="mw-headline" id="Execution_semantics">Execution semantics</span></h3>
                 |       <p>Once data has been specified, the machine must be instructed to perform operations on the data. The <i>execution semantics</i> of a language defines how and when the various constructs of a language should produce a program behaviour.</p>
                 |       <p>For example, the semantics may define the <!--del_lnk--> strategy by which expressions are evaluated to values, or the manner in which <!--del_lnk--> control structures conditionally execute statements.</p>
                 |       <h3><span class="mw-headline" id="Core_library">Core library</span></h3>
                 |       <p>Most programming languages have an associated <!--del_lnk--> core library (sometimes known as the 'Standard library', especially if it is included as part of the published language standard), which is conventionally made available by all implementations of the language. Core libraries typically include definitions for commonly used algorithms, data structures, and mechanisms for input and output.</p>
                 |       <p>A language's core library is often treated as part of the language by its users, although the designers may have treated it as a separate entity. Many language specifications define a core that must be made available in all implementations, and in the case of standardized languages this core library may be required. The line between a language and its core library therefore differs from language to language. Indeed, some languages are designed so that the meanings of certain syntactic constructs cannot even be described without referring to the core library. For example, in <a href="../../wp/j/Java_%2528programming_language%2529.htm" title="Java (programming language)">Java</a>, a string literal is defined as an instance of the <tt>java.lang.String</tt> class; similarly, in <!--del_lnk--> Smalltalk, an <!--del_lnk--> anonymous function expression (a "block") constructs an instance of the library's <tt>BlockContext</tt> class. Conversely, <!--del_lnk--> Scheme contains multiple coherent subsets that suffice to construct the rest of the language as library macros, and so the language designers do not even bother to say which portions of the language must be implemented as language constructs, and which must be implemented as parts of a library.</p>
                 |       <h2><span class="mw-headline" id="Practice">Practice</span></h2>
                 |       <p>A language's designers and users must construct a number of artifacts that govern and enable the practice of programming. The most important of these artifacts are the language <i>specification</i> and <i>implementation</i>.</p>
                 |       <h3><span class="mw-headline" id="Specification">Specification</span></h3>
                 |       <p>The <b>specification</b> of a programming language is intended to provide a definition that the language <!--del_lnk--> users and the <!--del_lnk--> implementors can use to determine whether the behaviour of a <!--del_lnk--> program is correct, given its <!--del_lnk--> source code.</p>
                 |       <p>A programming language specification can take several forms, including the following:</p>
                 |       <ul>
                 |        <li>An explicit definition of the syntax, static semantics, and execution semantics of the language. While syntax is commonly specified using a formal grammar, semantic definitions may be written in <!--del_lnk--> natural language (e.g., the <a href="../../wp/c/C_%2528programming_language%2529.htm" title="C (programming language)">C language</a>), or a <!--del_lnk--> formal semantics (e.g., the <!--del_lnk--> Standard ML and <!--del_lnk--> Scheme specifications).</li>
                 |        <li>A description of the behaviour of a <!--del_lnk--> translator for the language (e.g., the <a href="../../wp/c/C%252B%252B.htm" title="C++">C++</a> and <!--del_lnk--> Fortran specifications). The syntax and semantics of the language have to be inferred from this description, which may be written in natural or a formal language.</li>
                 |        <li>A <i>reference</i> or <i>model</i> implementation, sometimes written in the language being specified (e.g., <!--del_lnk--> Prolog or <!--del_lnk--> ANSI REXX). The syntax and semantics of the language are explicit in the behaviour of the reference implementation.</li>
                 |       </ul>
                 |       <h3><span class="mw-headline" id="Implementation">Implementation</span></h3>
                 |       <p>An <b>implementation</b> of a programming language provides a way to execute that program on one or more configurations of hardware and software. There are, broadly, two approaches to programming language implementation: <i><!--del_lnk--> compilation</i> and <i><!--del_lnk--> interpretation</i>. It is generally possible to implement a language using either technique.</p>
                 |       <p>The output of a <!--del_lnk--> compiler may be executed by hardware or a program called an interpreter. In some implementations that make use of the interpreter approach there is no distinct boundary between compiling and interpreting. For instance, some implementations of the <a class="mw-redirect" href="../../wp/b/BASIC.htm" title="BASIC programming language">BASIC programming language</a> compile and then execute the source a line at a time.</p>
                 |       <p>Programs that are executed directly on the hardware usually run several orders of magnitude faster than those that are interpreted in software.</p>
                 |       <p>One technique for improving the performance of interpreted programs is <!--del_lnk--> just-in-time compilation. Here the <!--del_lnk--> virtual machine, just before execution, translates the blocks of <!--del_lnk--> bytecode which are going to be used to machine code, for direct execution on the hardware.</p>
                 |       <h2><span class="mw-headline" id="History">History</span></h2>
                 |       <div class="thumb tright">
                 |        <div class="thumbinner" style="width:232px;"><a class="image" href="../../images/592/59268.jpg.htm"><img alt="" class="thumbimage" height="157" src="../../images/592/59268.jpg"  width="230" /></a><div class="thumbcaption">
                 |          <div class="magnify"><a class="internal" href="../../images/592/59268.jpg.htm" title="Enlarge"><img alt="" height="11" src="../../images/1x1white.gif" title="This image is not present because of licensing restrictions" width="15" /></a></div> A selection of textbooks that teach programming, in languages both popular and obscure. These are only a few of the thousands of programming languages and dialects that have been designed in history.</div>
                 |        </div>
                 |       </div>
                 |       <h3><span class="mw-headline" id="Early_developments">Early developments</span></h3>
                 |       <p>The first programming languages predate the modern computer. The 19th century had "programmable" <!--del_lnk--> looms and <!--del_lnk--> player piano scrolls which implemented what are today recognized as examples of <!--del_lnk--> domain-specific programming languages. By the beginning of the twentieth century, punch cards encoded data and directed mechanical processing. In the 1930s and 1940s, the formalisms of <!--del_lnk--> Alonzo Church's <!--del_lnk--> lambda calculus and <a href="../../wp/a/Alan_Turing.htm" title="Alan Turing">Alan Turing</a>'s <!--del_lnk--> Turing machines provided mathematical abstractions for expressing <a href="../../wp/a/Algorithm.htm" title="Algorithm">algorithms</a>; the lambda calculus remains influential in language design.</p>
                 |       <p>In the 1940s, the first electrically powered digital computers were created. The first <!--del_lnk--> high-level programming language to be designed for a computer was <!--del_lnk--> Plankalk&uuml;l, developed for the <a href="../../wp/g/Germany.htm" title="Germany">German</a> <!--del_lnk--> Z3 by <!--del_lnk--> Konrad Zuse between 1943 and 1945.</p>
                 |       <p>The computers of the early 1950s, notably the <!--del_lnk--> UNIVAC I and the <!--del_lnk--> IBM 701 used <!--del_lnk--> machine language programs. <!--del_lnk--> First generation machine language programming was quickly superseded by a <!--del_lnk--> second generation of programming languages known as <!--del_lnk--> Assembly languages. Later in the 1950s, assembly language programming, which had evolved to include the use of <!--del_lnk--> macro instructions, was followed by the development of three higher-level programming languages: <!--del_lnk--> FORTRAN, <!--del_lnk--> LISP, and <!--del_lnk--> COBOL. Updated versions of all of these are still in general use, and each has strongly influenced the development of later languages. At the end of the 1950s, the language formalized as <!--del_lnk--> Algol 60 was introduced, and most later programming languages are, in many respects, descendants of Algol. The format and use of the early programming languages was heavily influenced by the <!--del_lnk--> constraints of the interface.</p>
                 |       <h3><span class="mw-headline" id="Refinement">Refinement</span></h3>
                 |       <p>The period from the 1960s to the late 1970s brought the development of the major language paradigms now in use, though many aspects were refinements of ideas in the very first <!--del_lnk--> Third-generation programming languages:</p>
                 |       <ul>
                 |        <li><!--del_lnk--> APL introduced <i><!--del_lnk--> array programming</i> and influenced <!--del_lnk--> functional programming.</li>
                 |        <li><!--del_lnk--> PL/I (NPL) was designed in the early 1960s to incorporate the best ideas from FORTRAN and COBOL.</li>
                 |        <li>In the 1960s, <!--del_lnk--> Simula was the first language designed to support <i><!--del_lnk--> object-oriented programming</i>; in the mid-1970s, <!--del_lnk--> Smalltalk followed with the first "purely" object-oriented language.</li>
                 |        <li><a href="../../wp/c/C_%2528programming_language%2529.htm" title="C (programming language)">C</a> was developed between 1969 and 1973 as a <i><!--del_lnk--> systems programming</i> language, and remains popular.</li>
                 |        <li><!--del_lnk--> Prolog, designed in 1972, was the first <i><!--del_lnk--> logic programming</i> language.</li>
                 |        <li>In 1978, <!--del_lnk--> ML built a polymorphic type system on top of Lisp, pioneering <i><!--del_lnk--> statically typed <!--del_lnk--> functional programming</i> languages.</li>
                 |       </ul>
                 |       <p>Each of these languages spawned an entire family of descendants, and most modern languages count at least one of them in their ancestry.</p>
                 |       <p>The 1960s and 1970s also saw considerable debate over the merits of <i><!--del_lnk--> structured programming</i>, and whether programming languages should be designed to support it. <!--del_lnk--> Edsger Dijkstra, in a famous 1968 letter published in the <!--del_lnk--> Communications of the ACM, argued that <!--del_lnk--> GOTO statements should be eliminated from all "higher level" programming languages.</p>
                 |       <p>The 1960s and 1970s also saw expansion of techniques that reduced the footprint of a program as well as improved productivity of the programmer and user. The <!--del_lnk--> card deck for an early <!--del_lnk--> 4GL was a lot smaller for the same functionality expressed in a <!--del_lnk--> 3GL deck.</p>
                 |       <h3><span class="mw-headline" id="Consolidation_and_growth">Consolidation and growth</span></h3>
                 |       <p>The 1980s were years of relative consolidation. <a href="../../wp/c/C%252B%252B.htm" title="C++">C++</a> combined object-oriented and systems programming. The United States government standardized <!--del_lnk--> Ada, a systems programming language intended for use by defense contractors. In Japan and elsewhere, vast sums were spent investigating so-called <!--del_lnk--> "fifth generation" languages that incorporated logic programming constructs. The functional languages community moved to standardize ML and Lisp. Rather than inventing new paradigms, all of these movements elaborated upon the ideas invented in the previous decade.</p>
                 |       <p>One important trend in language design during the 1980s was an increased focus on programming for large-scale systems through the use of <i>modules</i>, or large-scale organizational units of code. <!--del_lnk--> Modula-2, Ada, and ML all developed notable module systems in the 1980s, although other languages, such as <!--del_lnk--> PL/I, already had extensive support for modular programming. Module systems were often wedded to <!--del_lnk--> generic programming constructs.</p>
                 |       <p>The rapid growth of the <a href="../../wp/i/Internet.htm" title="Internet">Internet</a> in the mid-1990's created opportunities for new languages. <a href="../../wp/p/Perl.htm" title="Perl">Perl</a>, originally a Unix scripting tool first released in 1987, became common in dynamic <!--del_lnk--> Web sites. <a href="../../wp/j/Java_%2528programming_language%2529.htm" title="Java (programming language)">Java</a> came to be used for server-side programming. These developments were not fundamentally novel, rather they were refinements to existing languages and paradigms, and largely based on the C family of programming languages.</p>
                 |       <p>Programming language evolution continues, in both industry and research. Current directions include security and reliability verification, new kinds of modularity (<!--del_lnk--> mixins, <!--del_lnk--> delegates, <!--del_lnk--> aspects), and database integration.</p>
                 |       <p>The <!--del_lnk--> 4GLs are examples of languages which are domain-specific, such as <!--del_lnk--> SQL, which manipulates and returns <!--del_lnk--> sets of data rather than the scalar values which are canonical to most programming languages. <a href="../../wp/p/Perl.htm" title="Perl">Perl</a>, for example, with its '<!--del_lnk--> here document' can hold multiple 4GL programs, as well as multiple JavaScript programs, in part of its own perl code and use variable interpolation in the 'here document' to support multi-language programming.</p>
                 |       <h3><span class="mw-headline" id="Measuring_language_usage">Measuring language usage</span></h3>
                 |       <p>It is difficult to determine which programming languages are most widely used, and what usage means varies by context. One language may occupy the greater number of programmer hours, a different one have more lines of code, and a third utilize the most CPU time. Some languages are very popular for particular kinds of applications. For example, <!--del_lnk--> COBOL is still strong in the corporate data centre, often on large <!--del_lnk--> mainframes; <!--del_lnk--> FORTRAN in engineering applications; <a href="../../wp/c/C_%2528programming_language%2529.htm" title="C (programming language)">C</a> in embedded applications and operating systems; and other languages are regularly used to write many different kinds of applications.</p>
                 |       <p>Various methods of measuring language popularity, each subject to a different bias over what is measured, have been proposed:</p>
                 |       <ul>
                 |        <li>counting the number of job advertisements that mention the language</li>
                 |        <li>the number of books sold that teach or describe the language</li>
                 |        <li>estimates of the number of existing lines of code written in the language&mdash;which may underestimate languages not often found in public searches</li>
                 |        <li>counts of language references found using a web search engine.</li>
                 |       </ul>
                 |       <h2><span class="mw-headline" id="Taxonomies">Taxonomies</span></h2>
                 |       <p>There is no overarching classification scheme for programming languages. A given programming language does not usually have a single ancestor language. Languages commonly arise by combining the elements of several predecessor languages with new ideas in circulation at the time. Ideas that originate in one language will diffuse throughout a family of related languages, and then leap suddenly across familial gaps to appear in an entirely different family.</p>
                 |       <p>The task is further complicated by the fact that languages can be classified along multiple axes. For example, Java is both an object-oriented language (because it encourages object-oriented organization) and a concurrent language (because it contains built-in constructs for running multiple <!--del_lnk--> threads in parallel). <a href="../../wp/p/Python_%2528programming_language%2529.htm" title="Python (programming language)">Python</a> is an object-oriented <!--del_lnk--> scripting language.</p>
                 |       <p>In broad strokes, programming languages divide into <i><!--del_lnk--> programming paradigms</i> and a classification by <i>intended domain of use</i>. Paradigms include <!--del_lnk--> procedural programming, <!--del_lnk--> object-oriented programming, <!--del_lnk--> functional programming, and <!--del_lnk--> logic programming; some languages are hybrids of paradigms or multi-paradigmatic. An <!--del_lnk--> assembly language is not so much a paradigm as a direct model of an underlying machine architecture. By purpose, programming languages might be considered general purpose, system programming languages, scripting languages, domain-specific languages, or concurrent/distributed languages (or a combination of these). Some general purpose languages were designed largely with educational goals.</p>
                 |       <p>A programming language may also be classified by factors unrelated to programming paradigm. For instance, most programming languages use <a href="../../wp/e/English_language.htm" title="English language">English language</a> keywords, while a <!--del_lnk--> minority do not. Other languages may be classified as being <!--del_lnk--> esoteric or not.</p>
                 |      </div>
                 |      <div class="printfooter"> Retrieved from "<!--del_lnk--> http://en.wikipedia.org/w/index.php?title=Programming_language&amp;oldid=229493360"</div>
                 |      <!-- end content -->
                 |      <div class="visualClear">
                 |      </div>
                 |     </div>
                 |    </div>
                 |   </div>
                 |   <div id="column-one">
                 |<div id="logo"><a href="../../index.htm"><img src="../../schools-wikipedia-logo.png" alt="Wikipedia for Schools" /></a></div>
                 |<div class="menu">
                 |<p class="sosheading"><a href="../../wp/index/subject.htm">Subjects</a></p>
                 |<p><a href="../../wp/index/subject.Art.htm">Art</a></p>
                 |<p><a href="../../wp/index/subject.Business_Studies.htm">Business Studies</a></p>
                 |<p><a href="../../wp/index/subject.Citizenship.htm">Citizenship</a></p>
                 |<p><a href="../../wp/index/subject.Countries.htm">Countries</a></p>
                 |<p><a href="../../wp/index/subject.Design_and_Technology.htm">Design and Technology</a></p>
                 |<p><a href="../../wp/index/subject.Everyday_life.htm">Everyday life</a></p>
                 |<p><a href="../../wp/index/subject.Geography.htm">Geography</a></p>
                 |<p><a href="../../wp/index/subject.History.htm">History</a></p>
                 |<p><a href="../../wp/index/subject.IT.htm">Information Technology</a></p>
                 |<p><a href="../../wp/index/subject.Language_and_literature.htm">Language and literature</a></p>
                 |<p><a href="../../wp/index/subject.Mathematics.htm">Mathematics</a></p>
                 |<p><a href="../../wp/index/subject.Music.htm">Music</a></p>
                 |<p><a href="../../wp/index/subject.People.htm">People</a></p>
                 |<p><a href="../../wp/index/subject.Portals.htm">Portals</a></p>
                 |<p><a href="../../wp/index/subject.Religion.htm">Religion</a></p>
                 |<p><a href="../../wp/index/subject.Science.htm">Science</a></p>
                 |<p style="margin-top: 10px;" class="sosheading"><a rel="nofollow" href="../../wp/index/alpha.htm">Title Word Index</a></p>
                 |</div>
                 |</div>
                 |
                 |   <div class="visualClear">
                 |   </div>
                 |   <div id="sosebar">
                 |    <div class="center"> Wikipedia for Schools is a selection taken from the original English-language Wikipedia by the child sponsorship charity <a rel="author" href="../../wp/s/Soschildrensvillages.htm">SOS Children</a>. It was created as a <a href="../../wp/w/Wikipedia_For_Schools.htm">checked and child-friendly teaching resource</a> for use in schools in the developing world and beyond.Sources and authors can be found at www.wikipedia.org. See also our <a href="../../disclaimer.htm"><strong>Disclaimer</strong></a>. These articles are available under the <a href="../../wp/w/Wikipedia%253AText_of_Creative_Commons_Attribution-ShareAlike_3.0_Unported_License.htm"> Creative Commons Attribution Share-Alike Version 3.0 Unported Licence</a>. This article was sourced from http://en.wikipedia.org/?oldid=229493360 . </div>
                 |   </div>
                 |  </div>
                 | </body>
                 |</html>
                 |"""

    val results = ReverseLinkGraph.findLinkDestinations(text).toList
    results.length should be (65)
  }
}