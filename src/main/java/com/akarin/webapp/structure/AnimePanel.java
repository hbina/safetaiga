package com.akarin.webapp.structure;

import com.akarin.webapp.util.Tools;

public class AnimePanel {

    private final String name;
    private final int episode, panel;
    private int weight = 0;

    public AnimePanel(final String name, final int episode, final int panel) {
        this.name = name;
        this.episode = episode;
        this.panel = panel;
    }

    public AnimePanel(final String name, final String episode, final String panel) {
        this.name = name;
        this.episode = Integer.valueOf(episode);
        this.panel = Integer.valueOf(panel);
    }

    public String getKey() {
        return "" + name + ":" + episode + ":" + panel;
    }

    public String getKey(final int degree) {
        if (degree == 1) {
            return "" + name;
        } else if (degree == 2) {
            return "" + name + ":" + episode;
        } else if (degree == 3) {
            return "" + name + ":" + episode + ":" + panel;
        }
        Tools.coutln("AnimePanel:ILLEGAL ARGUMENT:getKey");
        return null;
    }

    public String getName() {
        return name;
    }

    public int getEpisode() {
        return episode;
    }

    public int getPanel() {
        return panel;
    }

    public void incrementWeight() {
        weight++;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int newWeight) {
        weight = newWeight;
    }
}
