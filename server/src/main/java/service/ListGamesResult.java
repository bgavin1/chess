package service;

import java.util.Collection;

public record ListGamesResult(Collection<GameResultData> games) {
}
