package free.wordsextractor.bl;

import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.OnlyWordsDictionary;
import free.wordsextractor.bl.translation.Translation;
import free.wordsextractor.bl.translation.TranslationManager;
import free.wordsextractor.bl.translation.yandex.YandexTranslation;
import free.wordsextractor.common.tests.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@DisplayName("E2E Tests")
public class E2ETest {

    @DisplayName("E2E test with known words")
    @ParameterizedTest(name = "{index} => path={0}, fromLang={1}, toLang={2}")
    @CsvSource({"eng.txt, eng, ru"})
    public void e2eWithKnowns(String path, String langFrom, String langTo) {
        try {
            final FileManager fileMngr = new FileManager(Utils.getResourcePathStr(this, path));
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

            final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);

            Dictionary extractedWords = translationMngr.getExtractedWordsDict();
            extractedWords.removeWordsOfDict(new OnlyWordsDictionary(Paths.get(Utils.getResourcePathStr(this, "knowns.dict"))));
            List<String> words = extractedWords.getWords();

            Path apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this);
            Dictionary dict = new YandexTranslation(apiKeyPath, Translation.Langs.getLang(langFrom), Translation.Langs.getLang(langTo)).translate(words);
            String actual = dict.getSortedTranslations().toString();

            String expected = "[a [ə] pronoun\n" +
                    "\tодин, некий, каждый, какой-то\n" +
                    "\t(one, some, each) \n" +
                    "\n" +
                    "a [ə] adjective\n" +
                    "\tподобный, этакий\n" +
                    "\t(such) \n" +
                    "\tпрекрасно\n" +
                    "\n" +
                    "a [ə] noun\n" +
                    "\tпредмет\n" +
                    "\t(of) \n" +
                    "\n" +
                    ", add [æd] verb\n" +
                    "\tдобавлять, прибавлять, увеличивать, добавить, прибавить, увеличить\n" +
                    "\t(append, increase, gain) \n" +
                    "\tскладывать, сложить\n" +
                    "\t(put) \n" +
                    "\tдополнить, дополнять\n" +
                    "\t(complement) \n" +
                    "\tпридавать, придать, присоединить\n" +
                    "\t(give, attach) \n" +
                    "\tдобавиться\n" +
                    "\t(supplement) \n" +
                    "\tподлить, подливать, долить\n" +
                    "\t(pour) \n" +
                    "\tпополнять\n" +
                    "\t(fill) \n" +
                    "\tдописать\n" +
                    "\t(finish) \n" +
                    "\tприсовокупить\n" +
                    "\n" +
                    "add [æd] adjective\n" +
                    "\tдополнительный\n" +
                    "\t(additional) \n" +
                    "\n" +
                    ", an [æn] pronoun\n" +
                    "\tодин, любой, некий, иной, каждый\n" +
                    "\t(one, any, a, another, each) \n" +
                    "\n" +
                    "an [æn] preposition\n" +
                    "\tв, на, за\n" +
                    "\t(to, of) \n" +
                    "\n" +
                    "an [æn] conjunction\n" +
                    "\tесли\n" +
                    "\t(if) \n" +
                    "\n" +
                    ", and [ænd] conjunction\n" +
                    "\tи, а\n" +
                    "\t(or, as) \n" +
                    "\tа также\n" +
                    "\t(as well as) \n" +
                    "\tпричем, но\n" +
                    "\t(while, but) \n" +
                    "\n" +
                    "and [ænd] adverb\n" +
                    "\tтак и\n" +
                    "\t(as well as) \n" +
                    "\n" +
                    "and [ænd] preposition\n" +
                    "\tс\n" +
                    "\t(with) \n" +
                    "\n" +
                    ", annotation [ænəʊˈteɪʃn] noun\n" +
                    "\tкомментарий, Примечание, пояснение\n" +
                    "\t(commentary, explanation) \n" +
                    "\tаннотирование, комментирование\n" +
                    "\t(annotate, comment) \n" +
                    "\tАннотация, заметка\n" +
                    "\t(memo) \n" +
                    "\tпометка\n" +
                    "\t(tagging) \n" +
                    "\n" +
                    ", array [əˈreɪ] noun\n" +
                    "\tматрица\n" +
                    "\t(matrix) \n" +
                    "\tнабор, совокупность, множество, ряд\n" +
                    "\t(set, multitude, number) \n" +
                    "\tмасса\n" +
                    "\t(mass) \n" +
                    "\tмассив, элемент массива\n" +
                    "\t(solid, array element) \n" +
                    "\tрешетка, антенная решетка\n" +
                    "\t(lattice, antenna array) \n" +
                    "\tблок\n" +
                    "\t(block) \n" +
                    "\tгамма\n" +
                    "\t(range) \n" +
                    "\tбоевой порядок\n" +
                    "\t(formation) \n" +
                    "\n" +
                    "array [əˈreɪ] verb\n" +
                    "\tвыстраивать\n" +
                    "\t(rank) \n" +
                    "\tодеть\n" +
                    "\t(dress) \n" +
                    "\n" +
                    "array [əˈreɪ] foreign word\n" +
                    "\tarray\n" +
                    "\n" +
                    "array [əˈreɪ] adjective\n" +
                    "\tматричный\n" +
                    "\t(matrix) \n" +
                    "\n" +
                    ", by [baɪ] preposition\n" +
                    "\tпо, на, к, в, от, при, у, через, с, за, под\n" +
                    "\t(on, for, in, with, from, through) \n" +
                    "\tблагодаря\n" +
                    "\t(thanks) \n" +
                    "\n" +
                    "by [baɪ] adverb\n" +
                    "\tмимо\n" +
                    "\tс помощью\n" +
                    "\t(with) \n" +
                    "\tсо стороны\n" +
                    "\t(on the part) \n" +
                    "\n" +
                    ", can [kæn] verb\n" +
                    "\tмочь, смочь, уметь, суметь\n" +
                    "\n" +
                    "can [kæn] adjective\n" +
                    "\tспособный\n" +
                    "\tвозможно\n" +
                    "\n" +
                    "can [kæn] predicative\n" +
                    "\tможно, можно ли\n" +
                    "\n" +
                    "can [kæn] adverb\n" +
                    "\tв состоянии\n" +
                    "\n" +
                    "can [kæn] noun\n" +
                    "\tбанка\n" +
                    "\n" +
                    ", configure [kənˈfɪgər] verb\n" +
                    "\tформировать\n" +
                    "\t(create) \n" +
                    "\tнастроить, настраивать, установить\n" +
                    "\t(customize, install) \n" +
                    "\tсконфигурировать, конфигурировать, отконфигурировать\n" +
                    "\t(configurable) \n" +
                    "\tзадать\n" +
                    "\t(set) \n" +
                    "\n" +
                    ", data [ˈdeɪtə] noun\n" +
                    "\tданные, сведения, информация\n" +
                    "\t(information) \n" +
                    "\tобработка данных\n" +
                    "\t(data processing) \n" +
                    "\tхарактеристики\n" +
                    "\t(specs) \n" +
                    "\tпоказатели, параметры\n" +
                    "\t(metrics) \n" +
                    "\n" +
                    ", few [fjuː] numeral\n" +
                    "\tнесколько\n" +
                    "\n" +
                    "few [fjuː] adverb\n" +
                    "\tмало, немного\n" +
                    "\t(little) \n" +
                    "\n" +
                    "few [fjuː] pronoun\n" +
                    "\tнемногие, некоторые\n" +
                    "\t(some) \n" +
                    "\n" +
                    "few [fjuː] adjective\n" +
                    "\tнемногочисленный, малый, малочисленный\n" +
                    "\t(small) \n" +
                    "\n" +
                    "few [fjuː] noun\n" +
                    "\tнебольшое количество, незначительное число\n" +
                    "\t(small amount, small number) \n" +
                    "\n" +
                    "few [fjuː] predicative\n" +
                    "\tпочти нет\n" +
                    "\n" +
                    ", follow [ˈfɔləʊ] verb\n" +
                    "\tследовать, последовать, соответствовать, преследовать, проследовать, подчиняться\n" +
                    "\t(pursue, ensue, meet, proceed, obey) \n" +
                    "\tследить, проследить, отслеживать, уследить, прослеживать\n" +
                    "\t(watch, trace, track, keep an eye) \n" +
                    "\tсоблюдать, придерживаться, руководствоваться\n" +
                    "\t(observe, adhere, motivate) \n" +
                    "\tсопровождать, провожать\n" +
                    "\t(accompany) \n" +
                    "\tидти, пройти\n" +
                    "\t(go) \n" +
                    "\tвыполнять, проводить\n" +
                    "\t(perform, carry) \n" +
                    "\tвытекать\n" +
                    "\t(derive) \n" +
                    "\tдалее следовать\n" +
                    "\tповторять\n" +
                    "\t(repeat) \n" +
                    "\n" +
                    "follow [ˈfɔləʊ] adjective\n" +
                    "\tпоследующий\n" +
                    "\t(following) \n" +
                    "\n" +
                    ", have [hæv] verb\n" +
                    "\tиметь, содержать, иметься\n" +
                    "\t(include) \n" +
                    "\tобладать, располагать, пользоваться, владеть\n" +
                    "\t(possess, use) \n" +
                    "\tполучать, принимать\n" +
                    "\t(get, take) \n" +
                    "\tиспытывать\n" +
                    "\t(feel) \n" +
                    "\tпровести, проводить\n" +
                    "\t(hold) \n" +
                    "\tприходиться\n" +
                    "\t(account) \n" +
                    "\tоказывать\n" +
                    "\t(provide) \n" +
                    "\tносить\n" +
                    "\t(carry) \n" +
                    "\tпроявлять\n" +
                    "\t(show) \n" +
                    "\tпотерпеть\n" +
                    "\tпитать\n" +
                    "\t(supply) \n" +
                    "\n" +
                    "have [hæv] adverb\n" +
                    "\tуже\n" +
                    "\t(already) \n" +
                    "\n" +
                    "have [hæv] noun\n" +
                    "\tвспомогательный глагол\n" +
                    "\n" +
                    ", if [ɪf] conjunction\n" +
                    "\tесли, ежели\n" +
                    "\t(when) \n" +
                    "\tесли бы, будто\n" +
                    "\t(though, that) \n" +
                    "\n" +
                    "if [ɪf] preposition\n" +
                    "\tпри\n" +
                    "\t(when) \n" +
                    "\n" +
                    "if [ɪf] particle\n" +
                    "\tли\n" +
                    "\t(whether) \n" +
                    "\n" +
                    "if [ɪf] adverb\n" +
                    "\tкогда\n" +
                    "\t(once) \n" +
                    "\n" +
                    ", method [ˈmeθəd] noun\n" +
                    "\tметод, способ, методика, прием, подход, технология, путь, методология, техника\n" +
                    "\t(technique, way, approach, methodology) \n" +
                    "\tсредство\n" +
                    "\t(means) \n" +
                    "\n" +
                    "method [ˈmeθəd] adjective\n" +
                    "\tметодический\n" +
                    "\t(methodical) \n" +
                    "\n" +
                    ", multiple [ˈmʌltɪpl] adjective\n" +
                    "\tмногократный, многоразовый\n" +
                    "\t(repeated, reusable) \n" +
                    "\tразличный, многочисленный, разнообразный, неоднократный, разный, многообразный\n" +
                    "\t(different, numerous, various, diverse) \n" +
                    "\tсоставной\n" +
                    "\t(composite) \n" +
                    "\tмножественные\n" +
                    "\tкратное\n" +
                    "\t(integral multiple) \n" +
                    "\tкомплексный\n" +
                    "\t(complex) \n" +
                    "\tмноговариантный\n" +
                    "\t(multivariate) \n" +
                    "\tмногоэлементный\n" +
                    "\t(multielement) \n" +
                    "\n" +
                    "multiple [ˈmʌltɪpl] numeral\n" +
                    "\tнесколько\n" +
                    "\t(several) \n" +
                    "\tмного\n" +
                    "\t(many) \n" +
                    "\n" +
                    "multiple [ˈmʌltɪpl] noun\n" +
                    "\tмножество\n" +
                    "\t(many) \n" +
                    "\tмножественность\n" +
                    "\t(plurality) \n" +
                    "\tмногократная цепь\n" +
                    "\tмножитель\n" +
                    "\t(multiplier) \n" +
                    "\tкратное число\n" +
                    "\n" +
                    "multiple [ˈmʌltɪpl] adverb\n" +
                    "\tмногократно\n" +
                    "\t(many times) \n" +
                    "\n" +
                    ", of [ɔv] preposition\n" +
                    "\tиз, от, с, при, среди\n" +
                    "\t(from, with, among) \n" +
                    "\tо, в, об, относительно, по, на, за, к, для\n" +
                    "\t(on, in, about, for) \n" +
                    "\n" +
                    "of [ɔv] adverb\n" +
                    "\tв составе\n" +
                    "\t(as part) \n" +
                    "\tиз числа\n" +
                    "\t(among) \n" +
                    "\n" +
                    ", only [ˈəʊnlɪ] particle\n" +
                    "\tтолько, всего, лишь, просто, только лишь\n" +
                    "\t(just, all) \n" +
                    "\n" +
                    "only [ˈəʊnlɪ] adverb\n" +
                    "\tисключительно, единственно\n" +
                    "\t(exclusively, solely) \n" +
                    "\n" +
                    "only [ˈəʊnlɪ] adjective\n" +
                    "\tединственный\n" +
                    "\t(single) \n" +
                    "\n" +
                    "only [ˈəʊnlɪ] conjunction\n" +
                    "\tразве\n" +
                    "\t(unless) \n" +
                    "\n" +
                    ", or [ɔː] conjunction\n" +
                    "\tили, и, либо\n" +
                    "\t(and, either) \n" +
                    "\tиначе\n" +
                    "\t(lest) \n" +
                    "\n" +
                    ", our [ˈaʊə] pronoun\n" +
                    "\tнаш\n" +
                    "\t(ours) \n" +
                    "\n" +
                    ", parameterized [pəˈræmɪtəraɪzd] adjective\n" +
                    "\tпараметризованный, параметрический\n" +
                    "\t(parametrized, parametric) \n" +
                    "\n" +
                    ", pass [pɑːs] verb\n" +
                    "\tпройти, проходить, миновать\n" +
                    "\t(undergo, bypass) \n" +
                    "\tпередавать, передать\n" +
                    "\t(transmit, transfer) \n" +
                    "\tпереходить, перейти\n" +
                    "\t(go) \n" +
                    "\tсдать, сдавать\n" +
                    "\t(hand over, give) \n" +
                    "\tпроезжать, проехать\n" +
                    "\t(drive) \n" +
                    "\tпропускать, пропустить\n" +
                    "\t(skip) \n" +
                    "\tпринять\n" +
                    "\t(take) \n" +
                    "\tпроходить мимо\n" +
                    "\t(pass by) \n" +
                    "\tпролететь\n" +
                    "\t(fly) \n" +
                    "\n" +
                    "pass [pɑːs] noun\n" +
                    "\tпроход, пропуск, перевал\n" +
                    "\t(passage, skip, mountain pass) \n" +
                    "\tпасс, пас\n" +
                    "\t(pace) \n" +
                    "\n" +
                    "pass [pɑːs] adjective\n" +
                    "\tпроходной\n" +
                    "\t(entrance) \n" +
                    "\n" +
                    ", provided [prəˈvaɪdɪd] participle\n" +
                    "\tпредусмотренный, обеспечиваемый, установленный\n" +
                    "\t(stipulated, guaranteed, established) \n" +
                    "\tобеспеченный\n" +
                    "\t(secured) \n" +
                    "\tпредставленный, приведенный, содержащийся\n" +
                    "\t(represented, given, contained) \n" +
                    "\tснабженный, поставляемый\n" +
                    "\t(supplied) \n" +
                    "\tприлагаемый\n" +
                    "\t(attached) \n" +
                    "\tпредусматриваемый\n" +
                    "\t(foreseen) \n" +
                    "\n" +
                    "provided [prəˈvaɪdɪd] adverb\n" +
                    "\tпри условии\n" +
                    "\t(on condition) \n" +
                    "\n" +
                    "provided [prəˈvaɪdɪd] conjunction\n" +
                    "\tесли\n" +
                    "\t(if) \n" +
                    "\n" +
                    ", specify [ˈspesɪfaɪ] verb\n" +
                    "\tуказывать, обозначать, указать\n" +
                    "\t(indicate) \n" +
                    "\tзадавать, устанавливать, установить, задать\n" +
                    "\t(set) \n" +
                    "\tопределять, определить, описывать\n" +
                    "\t(determine, describe) \n" +
                    "\tспецифицировать\n" +
                    "\tпредусмотреть, оговорить, оговариваться, предусматривать, оговаривать\n" +
                    "\t(stipulate) \n" +
                    "\tуточнить, уточнять, конкретизировать\n" +
                    "\t(clarify, elaborate) \n" +
                    "\tрегламентировать\n" +
                    "\t(regulate) \n" +
                    "\tточно определять\n" +
                    "\t(pinpoint) \n" +
                    "\n" +
                    ", string [strɪŋ] noun\n" +
                    "\tстрока, вереница, шнур, строчка\n" +
                    "\t(line) \n" +
                    "\tструна, тетива\n" +
                    "\t(chord, bowstring) \n" +
                    "\tшнурок, завязка\n" +
                    "\t(lace, strap) \n" +
                    "\tверевка, веревочка, бечевка\n" +
                    "\t(rope, twine) \n" +
                    "\tцепочка\n" +
                    "\t(chain) \n" +
                    "\tнитка, нить, ниточка\n" +
                    "\t(thread) \n" +
                    "\tколонна\n" +
                    "\t(column) \n" +
                    "\tстроковое значение\n" +
                    "\t(string value) \n" +
                    "\tструнный инструмент\n" +
                    "\t(string instrument) \n" +
                    "\n" +
                    "string [strɪŋ] verb\n" +
                    "\tнанизывать, нанизать\n" +
                    "\t(bead) \n" +
                    "\n" +
                    "string [strɪŋ] adjective\n" +
                    "\tстроковый, струнный, строчный\n" +
                    "\t(line, stringed) \n" +
                    "\n" +
                    "string [strɪŋ] foreign word\n" +
                    "\tstrings\n" +
                    "\n" +
                    ", test [test] noun\n" +
                    "\tиспытание, тест, проверка, проба, тестирование, экзамен\n" +
                    "\t(testing, benchmark, check, sample, exam) \n" +
                    "\tанализ\n" +
                    "\t(analysis) \n" +
                    "\tобследование\n" +
                    "\t(examination) \n" +
                    "\n" +
                    "test [test] verb\n" +
                    "\tтестировать, проверять, протестировать, проверить, опробовать, апробировать\n" +
                    "\t(probe, check, try, approve) \n" +
                    "\tиспытывать, испытать\n" +
                    "\t(experience) \n" +
                    "\n" +
                    "test [test] adjective\n" +
                    "\tиспытательный, пробный, контрольный, тестовый\n" +
                    "\t(testing, trial, check) \n" +
                    "\tопытный\n" +
                    "\t(experimental) \n" +
                    "\n" +
                    ", the [ðiː] pronoun\n" +
                    "\tтот, этот, такой\n" +
                    "\t(this) \n" +
                    "\tчем\n" +
                    "\n" +
                    "the [ðiː] \n" +
                    "\tthe\n" +
                    "\n" +
                    "the [ðiː] conjunction\n" +
                    "\tтем\n" +
                    "\n" +
                    "the [ðiː] participle\n" +
                    "\tтак называемый\n" +
                    "\n" +
                    ", these [ðiːz] pronoun\n" +
                    "\tэти, они, сии\n" +
                    "\n" +
                    ", this [ðɪs] pronoun\n" +
                    "\tэтот, сей, такой\n" +
                    "\t(the) \n" +
                    "\tто\n" +
                    "\t(it) \n" +
                    "\n" +
                    "this [ðɪs] particle\n" +
                    "\tэто\n" +
                    "\n" +
                    "this [ðɪs] adjective\n" +
                    "\tнастоящий, нынешний\n" +
                    "\t(present, current) \n" +
                    "\n" +
                    "this [ðɪs] adverb\n" +
                    "\tтак\n" +
                    "\t(so) \n" +
                    "\n" +
                    ", to [tuː] preposition\n" +
                    "\tк, на, для, в, по, о, за\n" +
                    "\t(for, in) \n" +
                    "\tс, до, от, перед\n" +
                    "\t(up, from, before) \n" +
                    "\n" +
                    "to [tuː] conjunction\n" +
                    "\tчтобы, для того чтобы, дабы\n" +
                    "\n" +
                    "to [tuː] particle\n" +
                    "\tбы\n" +
                    "\n" +
                    "to [tuː] adjective\n" +
                    "\tдолжно\n" +
                    "\n" +
                    "to [tuː] pronoun\n" +
                    "\tкоторый\n" +
                    "\t(which) \n" +
                    "\n" +
                    "to [tuː] adverb\n" +
                    "\tс целью\n" +
                    "\t(for the purpose) \n" +
                    "\n" +
                    ", used [juːzd] participle\n" +
                    "\tиспользуемый, употребляемый, употребленный\n" +
                    "\t(utilized, consumed) \n" +
                    "\tприменяемый\n" +
                    "\t(applied) \n" +
                    "\tподержанный\n" +
                    "\t(secondhand) \n" +
                    "\tпривыкший\n" +
                    "\t(accustomed) \n" +
                    "\tотработанный, израсходованный\n" +
                    "\t(spent) \n" +
                    "\tбывший в употреблении\n" +
                    "\n" +
                    "used [juːzd] verb\n" +
                    "\tпривыкать\n" +
                    "\t(get) \n" +
                    "\tбывало\n" +
                    "\t(sometimes) \n" +
                    "\n" +
                    "used [juːzd] adverb\n" +
                    "\tобычно\n" +
                    "\t(typically) \n" +
                    "\n" +
                    ", using [ˈjuːzɪŋ] adverb\n" +
                    "\tс помощью, с использованием, при помощи\n" +
                    "\t(with, by using, by) \n" +
                    "\n" +
                    "using [ˈjuːzɪŋ] participle\n" +
                    "\tиспользующий, пользующийся, применяющий, употребляющий\n" +
                    "\t(utilizing, enjoying, applying, user) \n" +
                    "\n" +
                    ", we [wiː] pronoun\n" +
                    "\tмы\n" +
                    "\n" +
                    ", when [wen] conjunction\n" +
                    "\tкогда, после того как\n" +
                    "\t(while, after) \n" +
                    "\tесли\n" +
                    "\t(if) \n" +
                    "\n" +
                    "when [wen] adverb\n" +
                    "\tкак только\n" +
                    "\t(once) \n" +
                    "\tкак\n" +
                    "\t(as) \n" +
                    "\tпосле того\n" +
                    "\t(after) \n" +
                    "\n" +
                    "when [wen] preposition\n" +
                    "\tпри\n" +
                    "\t(if) \n" +
                    "\n" +
                    "]";

            Assert.assertEquals(expected, actual);

            List<String> notTranslated = dict.getNotTranslatedWords();
            notTranslated.sort(String::compareToIgnoreCase);
            Assert.assertEquals("[csvsource, is, methods, objects, parameters, rules]", notTranslated.toString());
        }
        catch (WordsExtractorException | URISyntaxException | IOException e) {
            System.err.println("Running interrupted by exception " + e);
        }
    }
}
