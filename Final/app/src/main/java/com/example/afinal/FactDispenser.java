package com.example.afinal;

import android.content.Context;


import java.util.ArrayList;
import java.util.Random;

public class FactDispenser {

    private static FactDispenser _fd;

    private Context _context;
    private ArrayList<Fact> _catFacts;
    private ArrayList<Fact> _dogFacts;

    private Random random = new Random();

    private FactDispenser(Context c){
        _context = c;
        _catFacts = FactsXMLParser.parseFacts(_context, FactsXMLParser.FACTS_FILE_CAT);
    }

    public static FactDispenser getInstance(Context c){
        if(_fd == null){
            _fd = new FactDispenser(c);
        }
        return _fd;
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
