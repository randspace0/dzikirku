package id.gritlabs.dzikirku.util.tajweed.model;

import id.gritlabs.dzikirku.util.tajweed.rule.GhunnaRule;
import id.gritlabs.dzikirku.util.tajweed.rule.IkhfaRule;
import id.gritlabs.dzikirku.util.tajweed.rule.IqlabRule;
import id.gritlabs.dzikirku.util.tajweed.rule.MeemRule;
import id.gritlabs.dzikirku.util.tajweed.rule.QalqalahRule;
import id.gritlabs.dzikirku.util.tajweed.rule.Rule;

import java.util.Arrays;
import java.util.List;

public class TajweedRule {

  public static final List<TajweedRule> MADANI_RULES = Arrays.asList(
      new TajweedRule(new GhunnaRule(false)),
//      new TajweedRule(new IdghamRule(false)),
      new TajweedRule(new IkhfaRule(false)),
      new TajweedRule(new IqlabRule(false)),
      new TajweedRule(new MeemRule(false)),
      new TajweedRule(new QalqalahRule(false))
//      new TajweedRule(new MaadRule(false))
  );

  public static final List<TajweedRule> NASKH_RULES = Arrays.asList(
      new TajweedRule(new GhunnaRule(true)),
//      new TajweedRule(new IdghamRule(true)),
      new TajweedRule(new IkhfaRule(true)),
      new TajweedRule(new IqlabRule(true)),
      new TajweedRule(new MeemRule(true)),
      new TajweedRule(new QalqalahRule(true))
//      new TajweedRule(new MaadRule(true))
  );

  public final Rule rule;

  public TajweedRule(Rule rule) {
    this.rule = rule;
  }
}
