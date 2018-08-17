package com.akarin.webapp.structure;

public class AnimeObject {

    private final String name;
    private final int numberOfEpisodes;
    int[] panels = null;

    public AnimeObject(final String name, final int numberOfEpisodes, final int numberOfPanels) {
        this.name = name;
        this.numberOfEpisodes = numberOfEpisodes;
        this.panels = new int[numberOfPanels];
    }

    public AnimeObject(final String name, final int numberOfEpisodes) {
        this.name = name;
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int[] getPanels() {
        return panels;
    }

    public void setPanels(final int[] givenPanels) {
        panels = givenPanels;
    }

    public void setNumberOfPanels(final int numberOfPanels) {
        this.panels = new int[numberOfPanels];
    }
}
