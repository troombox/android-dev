package com.example.afinal;

import android.content.Context;


import java.util.ArrayList;
import java.util.Random;

public class FactDispenser {
    Context _context;
    ArrayList<Fact> _catFacts;
    ArrayList<Fact> _dogFacts;
    Random random = new Random();

    public FactDispenser(Context c){
        _context = c;
        _catFacts = FactsXMLParser.parseFacts(_context, FactsXMLParser.FACTS_FILE_CAT);
    }

    public Fact getRandomFact(int factType){
        int maxInt;
        ArrayList<Fact> target;
        if(factType == Fact.FACT_TYPE_CAT){
            maxInt = _catFacts.size();
            target = _catFacts;
        }
        else{
            maxInt = _dogFacts.size();
            target = _dogFacts;
        }
        return target.get(random.nextInt(maxInt));
    }

    public Fact getFactByID(int factType, int id){
        ArrayList<Fact> target;
        if(factType == Fact.FACT_TYPE_CAT)
            target = _catFacts;
        else
            target = _dogFacts;
        for(Fact fc : target){
            if (fc.getFactID() == id){
                return fc;
            }
        }
        return null;
    }
}
