package com.altissia.clickandrun.domain.spreadsheet.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import java.util.Objects;

public class LAQuestionRow extends Row {

    @ExcelRow
    private int row;

    @ExcelCellName("UUID")
    private String uuid;

    @ExcelCellName("studyLg")
    private String studyLg;

    @ExcelCellName("level_EU")
    private String levelEu;

    @ExcelCellName("section")
    private String section;

    @ExcelCellName("online")
    private String online;

    @ExcelCellName("Topic")
    private String topic;

    @ExcelCellName("lexical domain")
    private String lexicalDomain;

    @ExcelCellName("grammar")
    private String grammar;

    @ExcelCellName("language function")
    private String languageFunction;

    @ExcelCellName("source")
    private String source;

    @ExcelCellName("type of comprehension")
    private String typeOfComprehension;

    @ExcelCellName("type")
    private String type;

    @ExcelCellName("instruction")
    private String instruction;

    @ExcelCellName("instruction_specific")
    private String instructionSpecific;

    @ExcelCellName("instruction_specific_gap_1")
    private String instructionSpecificGap1;

    @ExcelCellName("listening_comprehension_transcript")
    private String listeningComprehensionTranscript;

    @ExcelCellName("speaker")
    private String speaker;

    @ExcelCellName("reading_comprehension_text")
    private String readingComprehensionText;

    @ExcelCellName("question")
    private String question;

    @ExcelCellName("GAP1_correct_answer_1")
    private String gap1Correct1;

    @ExcelCellName("GAP1_correct_answer_2")
    private String gap1Correct2;

    @ExcelCellName("GAP1_correct_answer_3")
    private String gap1Correct3;

    @ExcelCellName("GAP1_correct_answer_4")
    private String gap1Correct4;

    @ExcelCellName("GAP1_correct_answer_5")
    private String gap1Correct5;

    @ExcelCellName("GAP2_correct_answer_1")
    private String gap2Correct1;

    @ExcelCellName("GAP2_correct_answer_2")
    private String gap2Correct2;

    @ExcelCellName("GAP2_correct_answer_3")
    private String gap2Correct3;

    @ExcelCellName("GAP2_correct_answer_4")
    private String gap2Correct4;

    @ExcelCellName("GAP2_correct_answer_5")
    private String gap2Correct5;

    @ExcelCellName("GAP1_incorrect_answer_1")
    private String gap1Incorrect1;

    @ExcelCellName("GAP1_incorrect_answer_2")
    private String gap1Incorrect2;

    @ExcelCellName("GAP1_incorrect_answer_3")
    private String gap1Incorrect3;

    @ExcelCellName("GAP2_incorrect_answer_1")
    private String gap2Incorrect1;

    @ExcelCellName("GAP2_incorrect_answer_2")
    private String gap2Incorrect2;

    @ExcelCellName("GAP2_incorrect_answer_3")
    private String gap2Incorrect3;

    @ExcelCellName("UUID_sound")
    private String uuidSound;

    @ExcelCellName("sound_base_reference")
    private String soundBaseReference;

    @ExcelCellName("status_sound")
    private String statusSound;

    @ExcelCellName("encrypted_UUID")
    private String encryptedUuid;

    @ExcelCellName("instruction_key")
    private String instructionKey;

    @ExcelCellName("instruction_specific_key")
    private String instructionSpecificKey;

    public String getUuid() {
        return uuid;
    }

    public String getStudyLg() {
        return studyLg;
    }

    public String getLevelEu() {
        return levelEu;
    }

    public String getSection() {
        return section;
    }

    public String getOnline() {
        return online;
    }

    public String getTopic() {
        return topic;
    }

    public String getLexicalDomain() {
        return lexicalDomain;
    }

    public String getGrammar() {
        return grammar;
    }

    public String getLanguageFunction() {
        return languageFunction;
    }

    public String getSource() {
        return source;
    }

    public String getTypeOfComprehension() {
        return typeOfComprehension;
    }

    public String getType() {
        return type;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getInstructionSpecific() {
        return instructionSpecific;
    }

    public String getInstructionSpecificGap1() {
        return instructionSpecificGap1;
    }

    public String getListeningComprehensionTranscript() {
        return listeningComprehensionTranscript;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getReadingComprehensionText() {
        return readingComprehensionText;
    }

    public String getQuestion() {
        return question;
    }

    public String getGap1Correct1() {
        return gap1Correct1;
    }

    public String getGap1Correct2() {
        return gap1Correct2;
    }

    public String getGap1Correct3() {
        return gap1Correct3;
    }

    public String getGap1Correct4() {
        return gap1Correct4;
    }

    public String getGap1Correct5() {
        return gap1Correct5;
    }

    public String getGap2Correct1() {
        return gap2Correct1;
    }

    public String getGap2Correct2() {
        return gap2Correct2;
    }

    public String getGap2Correct3() {
        return gap2Correct3;
    }

    public String getGap2Correct4() {
        return gap2Correct4;
    }

    public String getGap2Correct5() {
        return gap2Correct5;
    }

    public String getGap1Incorrect1() {
        return gap1Incorrect1;
    }

    public String getGap1Incorrect2() {
        return gap1Incorrect2;
    }

    public String getGap1Incorrect3() {
        return gap1Incorrect3;
    }

    public String getGap2Incorrect1() {
        return gap2Incorrect1;
    }

    public String getGap2Incorrect2() {
        return gap2Incorrect2;
    }

    public String getGap2Incorrect3() {
        return gap2Incorrect3;
    }

    public String getUuidSound() {
        return uuidSound;
    }

    public String getSoundBaseReference() {
        return soundBaseReference;
    }

    public String getStatusSound() {
        return statusSound;
    }

    public String getEncryptedUuid() {
        return encryptedUuid;
    }

    public String getInstructionKey() {
        return instructionKey;
    }

    public String getInstructionSpecificKey() {
        return instructionSpecificKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LAQuestionRow)) return false;
        LAQuestionRow that = (LAQuestionRow) o;
        return getRow() == that.getRow() &&
            Objects.equals(uuid, that.uuid) &&
            studyLg.equals(that.studyLg) &&
            levelEu.equals(that.levelEu) &&
            section.equals(that.section) &&
            online.equals(that.online) &&
            topic.equals(that.topic) &&
            Objects.equals(lexicalDomain, that.lexicalDomain) &&
            Objects.equals(grammar, that.grammar) &&
            Objects.equals(languageFunction, that.languageFunction) &&
            Objects.equals(source, that.source) &&
            Objects.equals(typeOfComprehension, that.typeOfComprehension) &&
            Objects.equals(type, that.type) &&
            Objects.equals(instruction, that.instruction) &&
            Objects.equals(instructionSpecific, that.instructionSpecific) &&
            Objects.equals(instructionSpecificGap1, that.instructionSpecificGap1) &&
            Objects.equals(listeningComprehensionTranscript, that.listeningComprehensionTranscript) &&
            Objects.equals(speaker, that.speaker) &&
            Objects.equals(readingComprehensionText, that.readingComprehensionText) &&
            Objects.equals(question, that.question) &&
            Objects.equals(gap1Correct1, that.gap1Correct1) &&
            Objects.equals(gap1Correct2, that.gap1Correct2) &&
            Objects.equals(gap1Correct3, that.gap1Correct3) &&
            Objects.equals(gap1Correct4, that.gap1Correct4) &&
            Objects.equals(gap1Correct5, that.gap1Correct5) &&
            Objects.equals(gap2Correct1, that.gap2Correct1) &&
            Objects.equals(gap2Correct2, that.gap2Correct2) &&
            Objects.equals(gap2Correct3, that.gap2Correct3) &&
            Objects.equals(gap2Correct4, that.gap2Correct4) &&
            Objects.equals(gap2Correct5, that.gap2Correct5) &&
            Objects.equals(gap1Incorrect1, that.gap1Incorrect1) &&
            Objects.equals(gap1Incorrect2, that.gap1Incorrect2) &&
            Objects.equals(gap1Incorrect3, that.gap1Incorrect3) &&
            Objects.equals(gap2Incorrect1, that.gap2Incorrect1) &&
            Objects.equals(gap2Incorrect2, that.gap2Incorrect2) &&
            Objects.equals(gap2Incorrect3, that.gap2Incorrect3) &&
            Objects.equals(uuidSound, that.uuidSound) &&
            Objects.equals(soundBaseReference, that.soundBaseReference) &&
            Objects.equals(statusSound, that.statusSound) &&
            Objects.equals(encryptedUuid, that.encryptedUuid) &&
            Objects.equals(instructionKey, that.instructionKey) &&
            Objects.equals(instructionSpecificKey, that.instructionSpecificKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), uuid, studyLg, levelEu, section, online, topic, lexicalDomain, grammar, languageFunction, source, typeOfComprehension, type, instruction, instructionSpecific, instructionSpecificGap1, listeningComprehensionTranscript, speaker, readingComprehensionText, question, gap1Correct1, gap1Correct2, gap1Correct3, gap1Correct4, gap1Correct5, gap2Correct1, gap2Correct2, gap2Correct3, gap2Correct4, gap2Correct5, gap1Incorrect1, gap1Incorrect2, gap1Incorrect3, gap2Incorrect1, gap2Incorrect2, gap2Incorrect3, uuidSound, soundBaseReference, statusSound, encryptedUuid, instructionKey, instructionSpecificKey);
    }

    @Override
    public String toString() {
        return "LAQuestionRow{" +
            "row=" + row +
            ", uuid='" + uuid + '\'' +
            ", studyLg='" + studyLg + '\'' +
            ", levelEu='" + levelEu + '\'' +
            ", section='" + section + '\'' +
            ", online='" + online + '\'' +
            ", topic='" + topic + '\'' +
            ", lexicalDomain='" + lexicalDomain + '\'' +
            ", grammar='" + grammar + '\'' +
            ", languageFunction='" + languageFunction + '\'' +
            ", source='" + source + '\'' +
            ", typeOfComprehension='" + typeOfComprehension + '\'' +
            ", type='" + type + '\'' +
            ", instruction='" + instruction + '\'' +
            ", instructionSpecific='" + instructionSpecific + '\'' +
            ", instructionSpecificGap1='" + instructionSpecificGap1 + '\'' +
            ", listeningComprehensionTranscript='" + listeningComprehensionTranscript + '\'' +
            ", speaker='" + speaker + '\'' +
            ", readingComprehensionText='" + readingComprehensionText + '\'' +
            ", question='" + question + '\'' +
            ", gap1Correct1='" + gap1Correct1 + '\'' +
            ", gap1Correct2='" + gap1Correct2 + '\'' +
            ", gap1Correct3='" + gap1Correct3 + '\'' +
            ", gap1Correct4='" + gap1Correct4 + '\'' +
            ", gap1Correct5='" + gap1Correct5 + '\'' +
            ", gap2Correct1='" + gap2Correct1 + '\'' +
            ", gap2Correct2='" + gap2Correct2 + '\'' +
            ", gap2Correct3='" + gap2Correct3 + '\'' +
            ", gap2Correct4='" + gap2Correct4 + '\'' +
            ", gap2Correct5='" + gap2Correct5 + '\'' +
            ", gap1Incorrect1='" + gap1Incorrect1 + '\'' +
            ", gap1Incorrect2='" + gap1Incorrect2 + '\'' +
            ", gap1Incorrect3='" + gap1Incorrect3 + '\'' +
            ", gap2Incorrect1='" + gap2Incorrect1 + '\'' +
            ", gap2Incorrect2='" + gap2Incorrect2 + '\'' +
            ", gap2Incorrect3='" + gap2Incorrect3 + '\'' +
            ", uuidSound='" + uuidSound + '\'' +
            ", soundBaseReference='" + soundBaseReference + '\'' +
            ", statusSound='" + statusSound + '\'' +
            ", encryptedUuid='" + encryptedUuid + '\'' +
            ", instructionKey='" + instructionKey + '\'' +
            ", instructionSpecificKey='" + instructionSpecificKey + '\'' +
            '}';
    }

    @Override
    public int getRow() {
        return row;
    }
}
