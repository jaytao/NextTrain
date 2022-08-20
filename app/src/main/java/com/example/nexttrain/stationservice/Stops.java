package com.example.nexttrain.stationservice;

public enum Stops {
  G21S("Queens Plaza"),
  D14N("7th Ave"),
  A31N("14th Street 8th Ave");

  private final String name;

  Stops(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
