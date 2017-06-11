package io.github.edwinvanrooij.domain.engine;

import io.github.edwinvanrooij.domain.Bid;
import io.github.edwinvanrooij.domain.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eddy
 * on 6/11/17.
 */
public class GameResults {
    private CardType winningType;
    private List<ResultItem> resultItems;

    public CardType getWinningType() {
        return winningType;
    }

    public void setWinningType(CardType winningType) {
        this.winningType = winningType;
    }

    public GameResults(CardType winningType) {
        this.winningType = winningType;
        resultItems = new ArrayList<>();

    }

    public void addResultItem(ResultItem item) {
        resultItems.add(item);
    }

}
