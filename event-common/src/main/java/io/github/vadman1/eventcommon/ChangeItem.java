package io.github.vadman1.eventcommon;

public record ChangeItem(String field, Object oldValue, Object newValue) {
}
