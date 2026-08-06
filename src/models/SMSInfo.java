package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sanka
 */
public class SMSInfo {

    private int units;
    private int characters;
    private String remaining;
    private int charactersPerUnit;
    private String encoding;

    public SMSInfo(int units, int characters,
            String remaining,
            int charactersPerUnit,
            String encoding) {

        this.units = units;
        this.characters = characters;
        this.remaining = remaining;
        this.charactersPerUnit = charactersPerUnit;
        this.encoding = encoding;
    }

    public int getUnits() {
        return units;
    }

    public int getCharacters() {
        return characters;
    }

    public String getRemaining() {
        return remaining;
    }

    public int getCharactersPerUnit() {
        return charactersPerUnit;
    }

    public String getEncoding() {
        return encoding;
    }

}
