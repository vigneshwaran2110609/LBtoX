package com.example.LBtoX.DTO;
import java.util.*;
import com.example.LBtoX.models.*;
public class LetterboxdProfilesIDs {
    private List<Long> ids;
    private List<LetterboxdProfile> profiles;
    public LetterboxdProfilesIDs(List<Long> ids, List<LetterboxdProfile> profiles){
        this.ids = ids;
        this.profiles = profiles;
    }
    public List<Long> getIDs(){
        return this.ids;
    }
    public List<LetterboxdProfile> getProfiles(){
        return this.profiles;
    }

}
