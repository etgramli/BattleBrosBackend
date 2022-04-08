package de.etgramli.battlebros.model.card.effect.application;

/**
 * Determine to what cards the effect applies.
 */
public sealed interface EffectApplication permits DiagonalApplication, ElementApplication, FacingApplication, NeighborApplication, NoneApplication, SelfApplication {
}
