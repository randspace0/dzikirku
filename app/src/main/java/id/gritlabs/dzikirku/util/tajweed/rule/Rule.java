package id.gritlabs.dzikirku.util.tajweed.rule;

import id.gritlabs.dzikirku.util.tajweed.model.Result;

import java.util.List;

public interface Rule {
  List<Result> checkAyah(String ayah);
}
