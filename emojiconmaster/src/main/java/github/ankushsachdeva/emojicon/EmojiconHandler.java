/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package github.ankushsachdeva.emojicon;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.TextView;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {
    private EmojiconHandler() {
    }

    private static final SparseIntArray sEmojisMap = new SparseIntArray(846);
    private static final Map<String, Integer> msEmojisMap = new HashMap<String, Integer>();
    private static final SparseIntArray sSoftbanksMap = new SparseIntArray(471);
    public static final Map<String, String> convertingEmojisMap = new HashMap<String, String>();

    static {
        convertingEmojisMap.put(":\\)", ":grinning:");
        convertingEmojisMap.put(":D", ":smile:");
        convertingEmojisMap.put(":\\(", ":disappointed:");
    }

    static {

        //begin tab 1
        msEmojisMap.put(":grinning:", R.drawable.grinning);
        msEmojisMap.put(":grin:", R.drawable.grin);
        msEmojisMap.put(":joy:", R.drawable.joy);
        msEmojisMap.put(":smiley:", R.drawable.smiley);
        msEmojisMap.put(":smile:", R.drawable.smile);
        msEmojisMap.put(":sweat_smile:", R.drawable.sweat_smile);
        msEmojisMap.put(":satisfied:", R.drawable.laughing);
        msEmojisMap.put(":laughing:", R.drawable.laughing);
        msEmojisMap.put(":innocent:", R.drawable.innocent);
        msEmojisMap.put(":smiling_imp:", R.drawable.smiling_imp);
        msEmojisMap.put(":wink:", R.drawable.wink);
        msEmojisMap.put(":blush:", R.drawable.blush);
        msEmojisMap.put(":yum:", R.drawable.yum);
        msEmojisMap.put(":relieved:", R.drawable.relieved);
        msEmojisMap.put(":heart_eyes:", R.drawable.heart_eyes);
        msEmojisMap.put(":sunglasses:", R.drawable.sunglasses);
        msEmojisMap.put(":smirk:", R.drawable.smirk);
        msEmojisMap.put(":neutral_face:", R.drawable.neutral_face);
        msEmojisMap.put(":expressionless:", R.drawable.expressionless);
        msEmojisMap.put(":unamused:", R.drawable.unamused);
        msEmojisMap.put(":sweat:", R.drawable.sweat);
        msEmojisMap.put(":pensive:", R.drawable.pensive);
        msEmojisMap.put(":confused:", R.drawable.confused);
        msEmojisMap.put(":confounded:", R.drawable.confounded);
        msEmojisMap.put(":kissing:", R.drawable.kissing);
        msEmojisMap.put(":kissing_heart:", R.drawable.kissing_heart);
        msEmojisMap.put(":kissing_smiling_eyes:", R.drawable.kissing_smiling_eyes);
        msEmojisMap.put(":kissing_closed_eyes:", R.drawable.kissing_closed_eyes);
        msEmojisMap.put(":stuck_out_tongue:", R.drawable.stuck_out_tongue);
        msEmojisMap.put(":stuck_out_tongue_winking_eye:", R.drawable.stuck_out_tongue_winking_eye);
        msEmojisMap.put(":stuck_out_tongue_closed_eyes:", R.drawable.stuck_out_tongue_closed_eyes);
        msEmojisMap.put(":disappointed:", R.drawable.disappointed);
        msEmojisMap.put(":worried:", R.drawable.worried);
        msEmojisMap.put(":angry:", R.drawable.angry);
        msEmojisMap.put(":rage:", R.drawable.rage);
        msEmojisMap.put(":cry:", R.drawable.cry);
        msEmojisMap.put(":persevere:", R.drawable.persevere);
        msEmojisMap.put(":triumph:", R.drawable.triumph);
        msEmojisMap.put(":disappointed_relieved:", R.drawable.disappointed_relieved);
        msEmojisMap.put(":frowning:", R.drawable.frowning);
        msEmojisMap.put(":anguished:", R.drawable.anguished);
        msEmojisMap.put(":fearful:", R.drawable.fearful);
        msEmojisMap.put(":weary:", R.drawable.weary);
        msEmojisMap.put(":sleepy:", R.drawable.sleepy);
        msEmojisMap.put(":tired_face:", R.drawable.tired_face);
        msEmojisMap.put(":grimacing:", R.drawable.grimacing);
        msEmojisMap.put(":sob:", R.drawable.sob);
        msEmojisMap.put(":open_mouth:", R.drawable.open_mouth);
        msEmojisMap.put(":hushed:", R.drawable.hushed);
        msEmojisMap.put(":cold_sweat:", R.drawable.cold_sweat);
        msEmojisMap.put(":scream:", R.drawable.scream);
        msEmojisMap.put(":astonished:", R.drawable.astonished);
        msEmojisMap.put(":flushed:", R.drawable.flushed);
        msEmojisMap.put(":sleeping:", R.drawable.sleeping);
        msEmojisMap.put(":dizzy_face:", R.drawable.dizzy_face);
        msEmojisMap.put(":no_mouth:", R.drawable.no_mouth);
        msEmojisMap.put(":relaxed:", R.drawable.relaxed);
        msEmojisMap.put(":mask:", R.drawable.mask);
        msEmojisMap.put(":smile_cat:", R.drawable.smile_cat);
        msEmojisMap.put(":joy_cat:", R.drawable.joy_cat);
        msEmojisMap.put(":smiley_cat:", R.drawable.smiley_cat);
        msEmojisMap.put(":heart_eyes_cat:", R.drawable.heart_eyes_cat);
        msEmojisMap.put(":smirk_cat:", R.drawable.smirk_cat);
        msEmojisMap.put(":kissing_cat:", R.drawable.kissing_cat);
        msEmojisMap.put(":pouting_cat:", R.drawable.pouting_cat);
        msEmojisMap.put(":crying_cat_face:", R.drawable.crying_cat_face);
        msEmojisMap.put(":scream_cat:", R.drawable.scream_cat);
        msEmojisMap.put(":no_good:", R.drawable.no_good);
        msEmojisMap.put(":ok_woman:", R.drawable.ok_woman);
        msEmojisMap.put(":bow:", R.drawable.bow);
        msEmojisMap.put(":see_no_evil:", R.drawable.see_no_evil);
        msEmojisMap.put(":hear_no_evil:", R.drawable.hear_no_evil);
        msEmojisMap.put(":speak_no_evil:", R.drawable.speak_no_evil);
        msEmojisMap.put(":raising_hand:", R.drawable.raising_hand);
        msEmojisMap.put(":raised_hands:", R.drawable.raised_hands);
        msEmojisMap.put(":person_frowning:", R.drawable.person_frowning);
        msEmojisMap.put(":person_with_pouting_face:", R.drawable.person_with_pouting_face);
        msEmojisMap.put(":bust_in_silhouette:", R.drawable.bust_in_silhouette);
        msEmojisMap.put(":busts_in_silhouette:", R.drawable.busts_in_silhouette);
        msEmojisMap.put(":boy:", R.drawable.boy);
        msEmojisMap.put(":girl:", R.drawable.girl);
        msEmojisMap.put(":man:", R.drawable.man);
        msEmojisMap.put(":woman:", R.drawable.woman);
        msEmojisMap.put(":family:", R.drawable.family);
        msEmojisMap.put(":couple:", R.drawable.couple);
        msEmojisMap.put(":two_men_holding_hands:", R.drawable.two_men_holding_hands);
        msEmojisMap.put(":two_women_holding_hands:", R.drawable.two_women_holding_hands);
        msEmojisMap.put(":cop:", R.drawable.cop);
        msEmojisMap.put(":dancers:", R.drawable.dancers);
        msEmojisMap.put(":bride_with_veil:", R.drawable.bride_with_veil);
        msEmojisMap.put(":person_with_blond_hair:", R.drawable.person_with_blond_hair);
        msEmojisMap.put(":man_with_gua_pi_mao:", R.drawable.man_with_gua_pi_mao);
        msEmojisMap.put(":man_with_turban:", R.drawable.man_with_turban);
        msEmojisMap.put(":older_man:", R.drawable.older_man);
        msEmojisMap.put(":older_woman:", R.drawable.older_woman);
        msEmojisMap.put(":baby:", R.drawable.baby);
        msEmojisMap.put(":construction_worker:", R.drawable.construction_worker);
        msEmojisMap.put(":princess:", R.drawable.princess);
        msEmojisMap.put(":japanese_ogre:", R.drawable.japanese_ogre);
        msEmojisMap.put(":japanese_goblin:", R.drawable.japanese_goblin);
        msEmojisMap.put(":ghost:", R.drawable.ghost);
        msEmojisMap.put(":angel:", R.drawable.angel);
        msEmojisMap.put(":alien:", R.drawable.alien);
        msEmojisMap.put(":space_invader:", R.drawable.space_invader);
        msEmojisMap.put(":imp:", R.drawable.imp);
        msEmojisMap.put(":skull:", R.drawable.skull);
        msEmojisMap.put(":information_desk_person:", R.drawable.information_desk_person);
        msEmojisMap.put(":guardsman:", R.drawable.guardsman);
        //end tab 1

        //begin tab 3
        msEmojisMap.put(":hamburger:", R.drawable.hamburger);
        msEmojisMap.put(":coffee:", R.drawable.coffee);
        msEmojisMap.put(":pizza:", R.drawable.pizza);
        msEmojisMap.put(":meat_on_bone:", R.drawable.meat_on_bone);
        msEmojisMap.put(":poultry_leg:", R.drawable.poultry_leg);
        msEmojisMap.put(":rice_cracker:", R.drawable.rice_cracker);
        msEmojisMap.put(":rice_ball:", R.drawable.rice_ball);
        msEmojisMap.put(":rice:", R.drawable.rice);
        msEmojisMap.put(":curry:", R.drawable.curry);
        msEmojisMap.put(":ramen:", R.drawable.ramen);
        msEmojisMap.put(":spaghetti:", R.drawable.spaghetti);
        msEmojisMap.put(":bread:", R.drawable.bread);
        msEmojisMap.put(":fries:", R.drawable.fries);
        msEmojisMap.put(":sweet_potato:", R.drawable.sweet_potato);
        msEmojisMap.put(":dango:", R.drawable.dango);
        msEmojisMap.put(":oden:", R.drawable.oden);
        msEmojisMap.put(":sushi:", R.drawable.sushi);
        msEmojisMap.put(":fried_shrimp:", R.drawable.fried_shrimp);
        msEmojisMap.put(":fish_cake:", R.drawable.fish_cake);
        msEmojisMap.put(":icecream:", R.drawable.icecream);
        msEmojisMap.put(":shaved_ice:", R.drawable.shaved_ice);
        msEmojisMap.put(":ice_cream:", R.drawable.ice_cream);
        msEmojisMap.put(":doughnut:", R.drawable.doughnut);
        msEmojisMap.put(":cookie:", R.drawable.cookie);
        msEmojisMap.put(":chocolate_bar:", R.drawable.chocolate_bar);
        msEmojisMap.put(":candy:", R.drawable.candy);
        msEmojisMap.put(":lollipop:", R.drawable.lollipop);
        msEmojisMap.put(":custard:", R.drawable.custard);
        msEmojisMap.put(":honey_pot:", R.drawable.honey_pot);
        msEmojisMap.put(":cake:", R.drawable.cake);
        msEmojisMap.put(":bento:", R.drawable.bento);
        msEmojisMap.put(":stew:", R.drawable.stew);
        msEmojisMap.put(":egg:", R.drawable.egg);
        msEmojisMap.put(":fork_and_knife:", R.drawable.fork_and_knife);
        msEmojisMap.put(":tea:", R.drawable.tea);
        msEmojisMap.put(":sake:", R.drawable.sake);
        msEmojisMap.put(":wine_glass:", R.drawable.wine_glass);
        msEmojisMap.put(":cocktail:", R.drawable.cocktail);
        msEmojisMap.put(":tropical_drink:", R.drawable.tropical_drink);
        msEmojisMap.put(":beer:", R.drawable.beer);
        msEmojisMap.put(":honeybee:", R.drawable.beer);
        msEmojisMap.put(":beers:", R.drawable.beers);
        msEmojisMap.put(":baby_bottle:", R.drawable.baby_bottle);
        msEmojisMap.put(":mushroom:", R.drawable.mushroom);
        msEmojisMap.put(":tomato:", R.drawable.tomato);
        msEmojisMap.put(":eggplant:", R.drawable.eggplant);
        msEmojisMap.put(":grapes:", R.drawable.grapes);
        msEmojisMap.put(":melon:", R.drawable.melon);
        msEmojisMap.put(":watermelon:", R.drawable.watermelon);
        msEmojisMap.put(":tangerine:", R.drawable.tangerine);
        msEmojisMap.put(":lemon:", R.drawable.lemon);
        msEmojisMap.put(":banana:", R.drawable.banana);
        msEmojisMap.put(":pineapple:", R.drawable.pineapple);
        msEmojisMap.put(":apple:", R.drawable.apple);
        msEmojisMap.put(":green_apple:", R.drawable.green_apple);
        msEmojisMap.put(":pear:", R.drawable.pear);
        msEmojisMap.put(":peach:", R.drawable.peach);
        msEmojisMap.put(":cherries:", R.drawable.cherries);
        msEmojisMap.put(":strawberry:", R.drawable.strawberry);
        msEmojisMap.put(":chestnut:", R.drawable.chestnut);
        msEmojisMap.put(":seedling:", R.drawable.seedling);
        msEmojisMap.put(":evergreen_tree:", R.drawable.evergreen_tree);
        msEmojisMap.put(":deciduous_tree:", R.drawable.deciduous_tree);
        msEmojisMap.put(":palm_tree:", R.drawable.palm_tree);
        msEmojisMap.put(":cactus:", R.drawable.cactus);
        msEmojisMap.put(":tulip:", R.drawable.tulip);
        msEmojisMap.put(":cherry_blossom:", R.drawable.cherry_blossom);
        msEmojisMap.put(":rose:", R.drawable.rose);
        msEmojisMap.put(":hibiscus:", R.drawable.hibiscus);
        msEmojisMap.put(":sunflower:", R.drawable.sunflower);
        msEmojisMap.put(":blossom:", R.drawable.blossom);
        msEmojisMap.put(":corn:", R.drawable.corn);
        msEmojisMap.put(":ear_of_rice:", R.drawable.ear_of_rice);
        msEmojisMap.put(":herb:", R.drawable.herb);
        msEmojisMap.put(":four_leaf_clover:", R.drawable.four_leaf_clover);
        msEmojisMap.put(":maple_leaf:", R.drawable.maple_leaf);
        msEmojisMap.put(":fallen_leaf:", R.drawable.fallen_leaf);
        msEmojisMap.put(":leaves:", R.drawable.leaves);
        //end tab 3




        // Tab 2 Nature
        msEmojisMap.put(":rat:", R.drawable.rat);
        msEmojisMap.put(":mouse2:", R.drawable.mouse2);
        msEmojisMap.put(":ox:", R.drawable.ox);
        msEmojisMap.put(":water_buffalo:", R.drawable.water_buffalo);
        msEmojisMap.put(":cow2:", R.drawable.cow2);
        msEmojisMap.put(":tiger2:", R.drawable.tiger2);
        msEmojisMap.put(":leopard:", R.drawable.leopard);
        msEmojisMap.put(":rabbit2:", R.drawable.rabbit2);
        msEmojisMap.put(":cat2:", R.drawable.cat2);
        msEmojisMap.put(":dragon:", R.drawable.dragon);
        msEmojisMap.put(":crocodile:", R.drawable.crocodile);
        msEmojisMap.put(":whale2:", R.drawable.whale2);
        msEmojisMap.put(":snail:", R.drawable.snail);
        msEmojisMap.put(":snake:", R.drawable.snake);
        msEmojisMap.put(":racehorse:", R.drawable.racehorse);
        msEmojisMap.put(":ram:", R.drawable.ram);
        msEmojisMap.put(":goat:", R.drawable.goat);
        msEmojisMap.put(":sheep:", R.drawable.sheep);
        msEmojisMap.put(":monkey:", R.drawable.monkey);
        msEmojisMap.put(":rooster:", R.drawable.rooster);
        msEmojisMap.put(":chicken:", R.drawable.chicken);
        msEmojisMap.put(":dog2:", R.drawable.dog2);
        msEmojisMap.put(":pig2:", R.drawable.pig2);
        msEmojisMap.put(":boar:", R.drawable.boar);
        msEmojisMap.put(":elephant:", R.drawable.elephant);
        msEmojisMap.put(":octopus:", R.drawable.octopus);
        msEmojisMap.put(":shell:", R.drawable.shell);
        msEmojisMap.put(":bug:", R.drawable.bug);
        msEmojisMap.put(":ant:", R.drawable.ant);
        msEmojisMap.put(":bee:", R.drawable.bee);
        msEmojisMap.put(":beetle:", R.drawable.beetle);
        msEmojisMap.put(":fish:", R.drawable.fish);
        msEmojisMap.put(":tropical_fish:", R.drawable.tropical_fish);
        msEmojisMap.put(":blowfish:", R.drawable.blowfish);
        msEmojisMap.put(":turtle:", R.drawable.turtle);
        msEmojisMap.put(":hatching_chick:", R.drawable.hatching_chick);
        msEmojisMap.put(":baby_chick:", R.drawable.baby_chick);
        msEmojisMap.put(":hatched_chick:", R.drawable.hatched_chick);
        msEmojisMap.put(":bird:", R.drawable.bird);
        msEmojisMap.put(":penguin:", R.drawable.penguin);
        msEmojisMap.put(":koala:", R.drawable.koala);
        msEmojisMap.put(":poodle:", R.drawable.poodle);
        msEmojisMap.put(":dromedary_camel:", R.drawable.dromedary_camel);
        msEmojisMap.put(":camel:", R.drawable.camel);
        msEmojisMap.put(":dolphin:", R.drawable.dolphin);
        msEmojisMap.put(":flipper:", R.drawable.dolphin);
        msEmojisMap.put(":mouse:", R.drawable.mouse);
        msEmojisMap.put(":cow:", R.drawable.cow);
        msEmojisMap.put(":tiger:", R.drawable.tiger);
        msEmojisMap.put(":rabbit:", R.drawable.rabbit);
        msEmojisMap.put(":cat:", R.drawable.cat);
        msEmojisMap.put(":dragon_face:", R.drawable.dragon_face);
        msEmojisMap.put(":whale:", R.drawable.whale);
        msEmojisMap.put(":horse:", R.drawable.horse);
        msEmojisMap.put(":monkey_face:", R.drawable.monkey_face);
        msEmojisMap.put(":dog:", R.drawable.dog);
        msEmojisMap.put(":pig:", R.drawable.pig);
        msEmojisMap.put(":frog:", R.drawable.frog);
        msEmojisMap.put(":hamster:", R.drawable.hamster);
        msEmojisMap.put(":wolf:", R.drawable.wolf);
        msEmojisMap.put(":bear:", R.drawable.bear);
        msEmojisMap.put(":panda_face:", R.drawable.panda_face);
        msEmojisMap.put(":pig_nose:", R.drawable.pig_nose);
        msEmojisMap.put(":feet:", R.drawable.feet);
        msEmojisMap.put(":paw_prints:", R.drawable.feet);
        msEmojisMap.put(":eyes:", R.drawable.eyes);
        msEmojisMap.put(":ear:", R.drawable.ear);
        msEmojisMap.put(":nose:", R.drawable.nose);
        msEmojisMap.put(":lips:", R.drawable.lips);
        msEmojisMap.put(":tongue:", R.drawable.tongue);
        msEmojisMap.put(":point_up:", R.drawable.point_up);
        msEmojisMap.put(":fist:", R.drawable.fist);
        msEmojisMap.put(":hand:", R.drawable.hand_raisedhand);   // khac
        msEmojisMap.put(":raised_hand:", R.drawable.hand_raisedhand);
        msEmojisMap.put(":v:", R.drawable.v);
        msEmojisMap.put(":point_up_2:", R.drawable.point_up_2);
        msEmojisMap.put(":point_down:", R.drawable.point_down);
        msEmojisMap.put(":point_left:", R.drawable.point_left);
        msEmojisMap.put(":point_right:", R.drawable.point_right);
        msEmojisMap.put(":facepunch:", R.drawable.facepunch_punch);  // khac
        msEmojisMap.put(":punch:", R.drawable.facepunch_punch);
        msEmojisMap.put(":wave:", R.drawable.wave);
        msEmojisMap.put(":ok_hand:", R.drawable.ok_hand);
        msEmojisMap.put(":thumbsup:", R.drawable.thumbsup);
        msEmojisMap.put(":+1:", R.drawable.thumbsup);
        msEmojisMap.put(":thumbsdown:", R.drawable.thumbsdown);
        msEmojisMap.put(":-1:", R.drawable.thumbsdown);
        msEmojisMap.put(":clap:", R.drawable.clap);
        msEmojisMap.put(":open_hands:", R.drawable.open_hands);
        // End tab 2



        // Objects
        msEmojisMap.put(":chestnut:", R.drawable.chestnut);

        msEmojisMap.put(":seedling:", R.drawable.seedling);
        msEmojisMap.put(":evergreen_tree:", R.drawable.evergreen_tree);
        msEmojisMap.put(":deciduous_tree:", R.drawable.deciduous_tree);
        msEmojisMap.put(":palm_tree:", R.drawable.palm_tree);
        msEmojisMap.put(":cactus:", R.drawable.cactus);
        msEmojisMap.put(":tulip:", R.drawable.tulip);

        msEmojisMap.put(":cherry_blossom:", R.drawable.cherry_blossom);

        msEmojisMap.put(":rose:", R.drawable.rose);
        msEmojisMap.put(":four_leaf_clover:", R.drawable.four_leaf_clover);
        msEmojisMap.put(":maple_leaf:", R.drawable.maple_leaf);
        msEmojisMap.put(":fallen_leaf:", R.drawable.fallen_leaf);
        msEmojisMap.put(":leaves:", R.drawable.leaves);

        msEmojisMap.put(":mushroom:", R.drawable.mushroom);
        msEmojisMap.put(":tomato:", R.drawable.tomato);
        msEmojisMap.put(":eggplant:", R.drawable.eggplant);
        msEmojisMap.put(":grapes:", R.drawable.grapes);
        msEmojisMap.put(":melon:", R.drawable.melon);
        msEmojisMap.put(":watermelon:", R.drawable.watermelon);
        msEmojisMap.put(":pear:", R.drawable.pear);


        msEmojisMap.put(":peach:", R.drawable.peach);
        msEmojisMap.put(":cherries:", R.drawable.cherries);
        msEmojisMap.put(":strawberry:", R.drawable.strawberry);
        msEmojisMap.put(":hamburger:", R.drawable.hamburger);
        msEmojisMap.put(":pizza:", R.drawable.pizza);
        msEmojisMap.put(":meat_on_bone:", R.drawable.meat_on_bone);
        msEmojisMap.put(":poultry_leg:", R.drawable.poultry_leg);

        msEmojisMap.put(":rice_cracker:", R.drawable.rice_cracker);
        msEmojisMap.put(":rice_ball:", R.drawable.rice_ball);
        msEmojisMap.put(":sweet_potato:", R.drawable.sweet_potato);
        msEmojisMap.put(":dango:", R.drawable.dango);
        msEmojisMap.put(":oden:", R.drawable.oden);
        msEmojisMap.put(":sushi:", R.drawable.sushi);
        msEmojisMap.put(":fried_shrimp:", R.drawable.fried_shrimp);

        msEmojisMap.put(":fish_cake:", R.drawable.fish_cake);
        msEmojisMap.put(":icecream:", R.drawable.icecream);
        msEmojisMap.put(":shaved_ice:", R.drawable.shaved_ice);
        msEmojisMap.put(":ice_cream:", R.drawable.ice_cream);
        msEmojisMap.put(":doughnut:", R.drawable.doughnut);
        msEmojisMap.put(":cake:", R.drawable.cake);


        msEmojisMap.put(":bento:", R.drawable.bento);
        msEmojisMap.put(":stew:", R.drawable.stew);
        msEmojisMap.put(":egg:", R.drawable.egg);

        msEmojisMap.put(":fork_and_knife:", R.drawable.fork_and_knife);
        msEmojisMap.put(":tea:", R.drawable.tea);
        msEmojisMap.put(":sake:", R.drawable.sake);
        msEmojisMap.put(":wine_glass:", R.drawable.wine_glass);
        msEmojisMap.put(":cocktail:", R.drawable.cocktail);
        msEmojisMap.put(":tropical_drink:", R.drawable.tropical_drink);

        // People
//        sEmojisMap.put(0x1f604, R.drawable.emoji_1f604);
//        sEmojisMap.put(0x1f603, R.drawable.emoji_1f603);
//        sEmojisMap.put(0x1f600, R.drawable.emoji_1f600);
//        sEmojisMap.put(0x1f60a, R.drawable.emoji_1f60a);
//        sEmojisMap.put(0x263a, R.drawable.emoji_263a);
//        sEmojisMap.put(0x1f609, R.drawable.emoji_1f609);
      /*  sEmojisMap.put(0x1f60d, R.drawable.emoji_1f60d);
        sEmojisMap.put(0x1f618, R.drawable.emoji_1f618);
        sEmojisMap.put(0x1f61a, R.drawable.emoji_1f61a);
        sEmojisMap.put(0x1f617, R.drawable.emoji_1f617);
        sEmojisMap.put(0x1f619, R.drawable.emoji_1f619);
        sEmojisMap.put(0x1f61c, R.drawable.emoji_1f61c);
        sEmojisMap.put(0x1f61d, R.drawable.emoji_1f61d);
        sEmojisMap.put(0x1f61b, R.drawable.emoji_1f61b);
        sEmojisMap.put(0x1f633, R.drawable.emoji_1f633);
        sEmojisMap.put(0x1f601, R.drawable.emoji_1f601);
        sEmojisMap.put(0x1f614, R.drawable.emoji_1f614);
        sEmojisMap.put(0x1f60c, R.drawable.emoji_1f60c);
        sEmojisMap.put(0x1f612, R.drawable.emoji_1f612);
        sEmojisMap.put(0x1f61e, R.drawable.emoji_1f61e);
        sEmojisMap.put(0x1f623, R.drawable.emoji_1f623);
        sEmojisMap.put(0x1f622, R.drawable.emoji_1f622);
        sEmojisMap.put(0x1f602, R.drawable.emoji_1f602);
        sEmojisMap.put(0x1f62d, R.drawable.emoji_1f62d);
        sEmojisMap.put(0x1f62a, R.drawable.emoji_1f62a);
        sEmojisMap.put(0x1f625, R.drawable.emoji_1f625);
        sEmojisMap.put(0x1f630, R.drawable.emoji_1f630);
        sEmojisMap.put(0x1f605, R.drawable.emoji_1f605);
        sEmojisMap.put(0x1f613, R.drawable.emoji_1f613);
        sEmojisMap.put(0x1f629, R.drawable.emoji_1f629);
        sEmojisMap.put(0x1f62b, R.drawable.emoji_1f62b);
        sEmojisMap.put(0x1f628, R.drawable.emoji_1f628);
        sEmojisMap.put(0x1f631, R.drawable.emoji_1f631);
        sEmojisMap.put(0x1f620, R.drawable.emoji_1f620);
        sEmojisMap.put(0x1f621, R.drawable.emoji_1f621);
        sEmojisMap.put(0x1f624, R.drawable.emoji_1f624);
        sEmojisMap.put(0x1f616, R.drawable.emoji_1f616);
        sEmojisMap.put(0x1f606, R.drawable.emoji_1f606);
        sEmojisMap.put(0x1f60b, R.drawable.emoji_1f60b);
        sEmojisMap.put(0x1f637, R.drawable.emoji_1f637);
        sEmojisMap.put(0x1f60e, R.drawable.emoji_1f60e);
        sEmojisMap.put(0x1f634, R.drawable.emoji_1f634);
        sEmojisMap.put(0x1f635, R.drawable.emoji_1f635);
        sEmojisMap.put(0x1f632, R.drawable.emoji_1f632);
        sEmojisMap.put(0x1f61f, R.drawable.emoji_1f61f);
        sEmojisMap.put(0x1f626, R.drawable.emoji_1f626);
        sEmojisMap.put(0x1f627, R.drawable.emoji_1f627);
        sEmojisMap.put(0x1f608, R.drawable.emoji_1f608);
        sEmojisMap.put(0x1f47f, R.drawable.emoji_1f47f);
        sEmojisMap.put(0x1f62e, R.drawable.emoji_1f62e);
        sEmojisMap.put(0x1f62c, R.drawable.emoji_1f62c);
        sEmojisMap.put(0x1f610, R.drawable.emoji_1f610);
        sEmojisMap.put(0x1f615, R.drawable.emoji_1f615);
        sEmojisMap.put(0x1f62f, R.drawable.emoji_1f62f);
        sEmojisMap.put(0x1f636, R.drawable.emoji_1f636);
        sEmojisMap.put(0x1f607, R.drawable.emoji_1f607);
        sEmojisMap.put(0x1f60f, R.drawable.emoji_1f60f);
        sEmojisMap.put(0x1f611, R.drawable.emoji_1f611);
        sEmojisMap.put(0x1f472, R.drawable.emoji_1f472);
        sEmojisMap.put(0x1f473, R.drawable.emoji_1f473);
        sEmojisMap.put(0x1f46e, R.drawable.emoji_1f46e);
        sEmojisMap.put(0x1f477, R.drawable.emoji_1f477);
        sEmojisMap.put(0x1f482, R.drawable.emoji_1f482);
        sEmojisMap.put(0x1f476, R.drawable.emoji_1f476);
        sEmojisMap.put(0x1f466, R.drawable.emoji_1f466);
        sEmojisMap.put(0x1f467, R.drawable.emoji_1f467);
        sEmojisMap.put(0x1f468, R.drawable.emoji_1f468);
        sEmojisMap.put(0x1f469, R.drawable.emoji_1f469);
        sEmojisMap.put(0x1f474, R.drawable.emoji_1f474);
        sEmojisMap.put(0x1f475, R.drawable.emoji_1f475);
        sEmojisMap.put(0x1f471, R.drawable.emoji_1f471);
        sEmojisMap.put(0x1f47c, R.drawable.emoji_1f47c);
        sEmojisMap.put(0x1f478, R.drawable.emoji_1f478);
        sEmojisMap.put(0x1f63a, R.drawable.emoji_1f63a);
        sEmojisMap.put(0x1f638, R.drawable.emoji_1f638);
        sEmojisMap.put(0x1f63b, R.drawable.emoji_1f63b);
        sEmojisMap.put(0x1f63d, R.drawable.emoji_1f63d);
        sEmojisMap.put(0x1f63c, R.drawable.emoji_1f63c);
        sEmojisMap.put(0x1f640, R.drawable.emoji_1f640);
        sEmojisMap.put(0x1f63f, R.drawable.emoji_1f63f);
        sEmojisMap.put(0x1f639, R.drawable.emoji_1f639);
        sEmojisMap.put(0x1f63e, R.drawable.emoji_1f63e);
        sEmojisMap.put(0x1f479, R.drawable.emoji_1f479);
        sEmojisMap.put(0x1f47a, R.drawable.emoji_1f47a);
        sEmojisMap.put(0x1f648, R.drawable.emoji_1f648);
        sEmojisMap.put(0x1f649, R.drawable.emoji_1f649);
        sEmojisMap.put(0x1f64a, R.drawable.emoji_1f64a);
        sEmojisMap.put(0x1f480, R.drawable.emoji_1f480);
        sEmojisMap.put(0x1f47d, R.drawable.emoji_1f47d);
        sEmojisMap.put(0x1f4a9, R.drawable.emoji_1f4a9);
        sEmojisMap.put(0x1f525, R.drawable.emoji_1f525);
        sEmojisMap.put(0x2728, R.drawable.emoji_2728);
        sEmojisMap.put(0x1f31f, R.drawable.emoji_1f31f);
        sEmojisMap.put(0x1f4ab, R.drawable.emoji_1f4ab);
        sEmojisMap.put(0x1f4a5, R.drawable.emoji_1f4a5);
        sEmojisMap.put(0x1f4a2, R.drawable.emoji_1f4a2);
        sEmojisMap.put(0x1f4a6, R.drawable.emoji_1f4a6);
        sEmojisMap.put(0x1f4a7, R.drawable.emoji_1f4a7);
        sEmojisMap.put(0x1f4a4, R.drawable.emoji_1f4a4);
        sEmojisMap.put(0x1f4a8, R.drawable.emoji_1f4a8);
        sEmojisMap.put(0x1f442, R.drawable.emoji_1f442);
        sEmojisMap.put(0x1f440, R.drawable.emoji_1f440);
        sEmojisMap.put(0x1f443, R.drawable.emoji_1f443);
        sEmojisMap.put(0x1f445, R.drawable.emoji_1f445);
        sEmojisMap.put(0x1f444, R.drawable.emoji_1f444);
        sEmojisMap.put(0x1f44d, R.drawable.emoji_1f44d);
        sEmojisMap.put(0x1f44e, R.drawable.emoji_1f44e);
        sEmojisMap.put(0x1f44c, R.drawable.emoji_1f44c);
        sEmojisMap.put(0x1f44a, R.drawable.emoji_1f44a);
        sEmojisMap.put(0x270a, R.drawable.emoji_270a);
        sEmojisMap.put(0x270c, R.drawable.emoji_270c);
        sEmojisMap.put(0x1f44b, R.drawable.emoji_1f44b);
        sEmojisMap.put(0x270b, R.drawable.emoji_270b);
        sEmojisMap.put(0x1f450, R.drawable.emoji_1f450);
        sEmojisMap.put(0x1f446, R.drawable.emoji_1f446);
        sEmojisMap.put(0x1f447, R.drawable.emoji_1f447);
        sEmojisMap.put(0x1f449, R.drawable.emoji_1f449);
        sEmojisMap.put(0x1f448, R.drawable.emoji_1f448);
        sEmojisMap.put(0x1f64c, R.drawable.emoji_1f64c);
        sEmojisMap.put(0x1f64f, R.drawable.emoji_1f64f);
        sEmojisMap.put(0x261d, R.drawable.emoji_261d);
        sEmojisMap.put(0x1f44f, R.drawable.emoji_1f44f);
        sEmojisMap.put(0x1f4aa, R.drawable.emoji_1f4aa);
        sEmojisMap.put(0x1f6b6, R.drawable.emoji_1f6b6);
        sEmojisMap.put(0x1f3c3, R.drawable.emoji_1f3c3);
        sEmojisMap.put(0x1f483, R.drawable.emoji_1f483);
        sEmojisMap.put(0x1f46b, R.drawable.emoji_1f46b);
        sEmojisMap.put(0x1f46a, R.drawable.emoji_1f46a);
        sEmojisMap.put(0x1f46c, R.drawable.emoji_1f46c);
        sEmojisMap.put(0x1f46d, R.drawable.emoji_1f46d);
        sEmojisMap.put(0x1f48f, R.drawable.emoji_1f48f);
        sEmojisMap.put(0x1f491, R.drawable.emoji_1f491);
        sEmojisMap.put(0x1f46f, R.drawable.emoji_1f46f);
        sEmojisMap.put(0x1f646, R.drawable.emoji_1f646);
        sEmojisMap.put(0x1f645, R.drawable.emoji_1f645);
        sEmojisMap.put(0x1f481, R.drawable.emoji_1f481);
        sEmojisMap.put(0x1f64b, R.drawable.emoji_1f64b);
        sEmojisMap.put(0x1f486, R.drawable.emoji_1f486);
        sEmojisMap.put(0x1f487, R.drawable.emoji_1f487);
        sEmojisMap.put(0x1f485, R.drawable.emoji_1f485);
        sEmojisMap.put(0x1f470, R.drawable.emoji_1f470);
        sEmojisMap.put(0x1f64e, R.drawable.emoji_1f64e);
        sEmojisMap.put(0x1f64d, R.drawable.emoji_1f64d);
        sEmojisMap.put(0x1f647, R.drawable.emoji_1f647);
        sEmojisMap.put(0x1f3a9, R.drawable.emoji_1f3a9);
        sEmojisMap.put(0x1f451, R.drawable.emoji_1f451);
        sEmojisMap.put(0x1f452, R.drawable.emoji_1f452);
        sEmojisMap.put(0x1f45f, R.drawable.emoji_1f45f);
        sEmojisMap.put(0x1f45e, R.drawable.emoji_1f45e);
        sEmojisMap.put(0x1f461, R.drawable.emoji_1f461);
        sEmojisMap.put(0x1f460, R.drawable.emoji_1f460);
        sEmojisMap.put(0x1f462, R.drawable.emoji_1f462);
        sEmojisMap.put(0x1f455, R.drawable.emoji_1f455);
        sEmojisMap.put(0x1f454, R.drawable.emoji_1f454);
        sEmojisMap.put(0x1f45a, R.drawable.emoji_1f45a);
        sEmojisMap.put(0x1f457, R.drawable.emoji_1f457);
        sEmojisMap.put(0x1f3bd, R.drawable.emoji_1f3bd);
        sEmojisMap.put(0x1f456, R.drawable.emoji_1f456);
        sEmojisMap.put(0x1f458, R.drawable.emoji_1f458);
        sEmojisMap.put(0x1f459, R.drawable.emoji_1f459);
        sEmojisMap.put(0x1f4bc, R.drawable.emoji_1f4bc);
        sEmojisMap.put(0x1f45c, R.drawable.emoji_1f45c);
        sEmojisMap.put(0x1f45d, R.drawable.emoji_1f45d);
        sEmojisMap.put(0x1f45b, R.drawable.emoji_1f45b);
        sEmojisMap.put(0x1f453, R.drawable.emoji_1f453);
        sEmojisMap.put(0x1f380, R.drawable.emoji_1f380);
        sEmojisMap.put(0x1f302, R.drawable.emoji_1f302);
        sEmojisMap.put(0x1f484, R.drawable.emoji_1f484);
        sEmojisMap.put(0x1f49b, R.drawable.emoji_1f49b);
        sEmojisMap.put(0x1f499, R.drawable.emoji_1f499);
        sEmojisMap.put(0x1f49c, R.drawable.emoji_1f49c);
        sEmojisMap.put(0x1f49a, R.drawable.emoji_1f49a);
        sEmojisMap.put(0x2764, R.drawable.emoji_2764);
        sEmojisMap.put(0x1f494, R.drawable.emoji_1f494);
        sEmojisMap.put(0x1f497, R.drawable.emoji_1f497);
        sEmojisMap.put(0x1f493, R.drawable.emoji_1f493);
        sEmojisMap.put(0x1f495, R.drawable.emoji_1f495);
        sEmojisMap.put(0x1f496, R.drawable.emoji_1f496);
        sEmojisMap.put(0x1f49e, R.drawable.emoji_1f49e);
        sEmojisMap.put(0x1f498, R.drawable.emoji_1f498);
        sEmojisMap.put(0x1f48c, R.drawable.emoji_1f48c);
        sEmojisMap.put(0x1f48b, R.drawable.emoji_1f48b);
        sEmojisMap.put(0x1f48d, R.drawable.emoji_1f48d);
        sEmojisMap.put(0x1f48e, R.drawable.emoji_1f48e);
        sEmojisMap.put(0x1f464, R.drawable.emoji_1f464);
        sEmojisMap.put(0x1f465, R.drawable.emoji_1f465);
        sEmojisMap.put(0x1f4ac, R.drawable.emoji_1f4ac);
        sEmojisMap.put(0x1f463, R.drawable.emoji_1f463);
        sEmojisMap.put(0x1f4ad, R.drawable.emoji_1f4ad);

        // Nature
        sEmojisMap.put(0x1f436, R.drawable.emoji_1f436);
        sEmojisMap.put(0x1f43a, R.drawable.emoji_1f43a);
        sEmojisMap.put(0x1f431, R.drawable.emoji_1f431);
        sEmojisMap.put(0x1f42d, R.drawable.emoji_1f42d);
        sEmojisMap.put(0x1f439, R.drawable.emoji_1f439);
        sEmojisMap.put(0x1f430, R.drawable.emoji_1f430);
        sEmojisMap.put(0x1f438, R.drawable.emoji_1f438);
        sEmojisMap.put(0x1f42f, R.drawable.emoji_1f42f);
        sEmojisMap.put(0x1f428, R.drawable.emoji_1f428);
        sEmojisMap.put(0x1f43b, R.drawable.emoji_1f43b);
        sEmojisMap.put(0x1f437, R.drawable.emoji_1f437);
        sEmojisMap.put(0x1f43d, R.drawable.emoji_1f43d);
        sEmojisMap.put(0x1f42e, R.drawable.emoji_1f42e);
        sEmojisMap.put(0x1f417, R.drawable.emoji_1f417);
        sEmojisMap.put(0x1f435, R.drawable.emoji_1f435);
        sEmojisMap.put(0x1f412, R.drawable.emoji_1f412);
        sEmojisMap.put(0x1f434, R.drawable.emoji_1f434);
        sEmojisMap.put(0x1f411, R.drawable.emoji_1f411);
        sEmojisMap.put(0x1f418, R.drawable.emoji_1f418);
        sEmojisMap.put(0x1f43c, R.drawable.emoji_1f43c);
        sEmojisMap.put(0x1f427, R.drawable.emoji_1f427);
        sEmojisMap.put(0x1f426, R.drawable.emoji_1f426);
        sEmojisMap.put(0x1f424, R.drawable.emoji_1f424);
        sEmojisMap.put(0x1f425, R.drawable.emoji_1f425);
        sEmojisMap.put(0x1f423, R.drawable.emoji_1f423);
        sEmojisMap.put(0x1f414, R.drawable.emoji_1f414);
        sEmojisMap.put(0x1f40d, R.drawable.emoji_1f40d);
        sEmojisMap.put(0x1f422, R.drawable.emoji_1f422);
        sEmojisMap.put(0x1f41b, R.drawable.emoji_1f41b);
        sEmojisMap.put(0x1f41d, R.drawable.emoji_1f41d);
        sEmojisMap.put(0x1f41c, R.drawable.emoji_1f41c);
        sEmojisMap.put(0x1f41e, R.drawable.emoji_1f41e);
        sEmojisMap.put(0x1f40c, R.drawable.emoji_1f40c);
        sEmojisMap.put(0x1f419, R.drawable.emoji_1f419);
        sEmojisMap.put(0x1f41a, R.drawable.emoji_1f41a);
        sEmojisMap.put(0x1f420, R.drawable.emoji_1f420);
        sEmojisMap.put(0x1f41f, R.drawable.emoji_1f41f);
        sEmojisMap.put(0x1f42c, R.drawable.emoji_1f42c);
        sEmojisMap.put(0x1f433, R.drawable.emoji_1f433);
        sEmojisMap.put(0x1f40b, R.drawable.emoji_1f40b);
        sEmojisMap.put(0x1f404, R.drawable.emoji_1f404);
        sEmojisMap.put(0x1f40f, R.drawable.emoji_1f40f);
        sEmojisMap.put(0x1f400, R.drawable.emoji_1f400);
        sEmojisMap.put(0x1f403, R.drawable.emoji_1f403);
        sEmojisMap.put(0x1f405, R.drawable.emoji_1f405);
        sEmojisMap.put(0x1f407, R.drawable.emoji_1f407);
        sEmojisMap.put(0x1f409, R.drawable.emoji_1f409);
        sEmojisMap.put(0x1f40e, R.drawable.emoji_1f40e);
        sEmojisMap.put(0x1f410, R.drawable.emoji_1f410);
        sEmojisMap.put(0x1f413, R.drawable.emoji_1f413);
        sEmojisMap.put(0x1f415, R.drawable.emoji_1f415);
        sEmojisMap.put(0x1f416, R.drawable.emoji_1f416);
        sEmojisMap.put(0x1f401, R.drawable.emoji_1f401);
        sEmojisMap.put(0x1f402, R.drawable.emoji_1f402);
        sEmojisMap.put(0x1f432, R.drawable.emoji_1f432);
        sEmojisMap.put(0x1f421, R.drawable.emoji_1f421);
        sEmojisMap.put(0x1f40a, R.drawable.emoji_1f40a);
        sEmojisMap.put(0x1f42b, R.drawable.emoji_1f42b);
        sEmojisMap.put(0x1f42a, R.drawable.emoji_1f42a);
        sEmojisMap.put(0x1f406, R.drawable.emoji_1f406);
        sEmojisMap.put(0x1f408, R.drawable.emoji_1f408);
        sEmojisMap.put(0x1f429, R.drawable.emoji_1f429);
        sEmojisMap.put(0x1f43e, R.drawable.emoji_1f43e);
        sEmojisMap.put(0x1f490, R.drawable.emoji_1f490);
        sEmojisMap.put(0x1f338, R.drawable.emoji_1f338);
        sEmojisMap.put(0x1f337, R.drawable.emoji_1f337);
        sEmojisMap.put(0x1f340, R.drawable.emoji_1f340);
        sEmojisMap.put(0x1f339, R.drawable.emoji_1f339);
        sEmojisMap.put(0x1f33b, R.drawable.emoji_1f33b);
        sEmojisMap.put(0x1f33a, R.drawable.emoji_1f33a);
        sEmojisMap.put(0x1f341, R.drawable.emoji_1f341);
        sEmojisMap.put(0x1f343, R.drawable.emoji_1f343);
        sEmojisMap.put(0x1f342, R.drawable.emoji_1f342);
        sEmojisMap.put(0x1f33f, R.drawable.emoji_1f33f);
        sEmojisMap.put(0x1f33e, R.drawable.emoji_1f33e);
        sEmojisMap.put(0x1f344, R.drawable.emoji_1f344);
        sEmojisMap.put(0x1f335, R.drawable.emoji_1f335);
        sEmojisMap.put(0x1f334, R.drawable.emoji_1f334);
        sEmojisMap.put(0x1f332, R.drawable.emoji_1f332);
        sEmojisMap.put(0x1f333, R.drawable.emoji_1f333);
        sEmojisMap.put(0x1f330, R.drawable.emoji_1f330);
        sEmojisMap.put(0x1f331, R.drawable.emoji_1f331);
        sEmojisMap.put(0x1f33c, R.drawable.emoji_1f33c);
        sEmojisMap.put(0x1f310, R.drawable.emoji_1f310);
        sEmojisMap.put(0x1f31e, R.drawable.emoji_1f31e);
        sEmojisMap.put(0x1f31d, R.drawable.emoji_1f31d);
        sEmojisMap.put(0x1f31a, R.drawable.emoji_1f31a);
        sEmojisMap.put(0x1f311, R.drawable.emoji_1f311);
        sEmojisMap.put(0x1f312, R.drawable.emoji_1f312);
        sEmojisMap.put(0x1f313, R.drawable.emoji_1f313);
        sEmojisMap.put(0x1f314, R.drawable.emoji_1f314);
        sEmojisMap.put(0x1f315, R.drawable.emoji_1f315);
        sEmojisMap.put(0x1f316, R.drawable.emoji_1f316);
        sEmojisMap.put(0x1f317, R.drawable.emoji_1f317);
        sEmojisMap.put(0x1f318, R.drawable.emoji_1f318);
        sEmojisMap.put(0x1f31c, R.drawable.emoji_1f31c);
        sEmojisMap.put(0x1f31b, R.drawable.emoji_1f31b);
        sEmojisMap.put(0x1f319, R.drawable.emoji_1f319);
        sEmojisMap.put(0x1f30d, R.drawable.emoji_1f30d);
        sEmojisMap.put(0x1f30e, R.drawable.emoji_1f30e);
        sEmojisMap.put(0x1f30f, R.drawable.emoji_1f30f);
        sEmojisMap.put(0x1f30b, R.drawable.emoji_1f30b);
        sEmojisMap.put(0x1f30c, R.drawable.emoji_1f30c);
        sEmojisMap.put(0x1f320, R.drawable.emoji_1f303); // TODO (rockerhieu) review this emoji
        sEmojisMap.put(0x2b50, R.drawable.emoji_2b50);
        sEmojisMap.put(0x2600, R.drawable.emoji_2600);
        sEmojisMap.put(0x26c5, R.drawable.emoji_26c5);
        sEmojisMap.put(0x2601, R.drawable.emoji_2601);
        sEmojisMap.put(0x26a1, R.drawable.emoji_26a1);
        sEmojisMap.put(0x2614, R.drawable.emoji_2614);
        sEmojisMap.put(0x2744, R.drawable.emoji_2744);
        sEmojisMap.put(0x26c4, R.drawable.emoji_26c4);
        sEmojisMap.put(0x1f300, R.drawable.emoji_1f300);
        sEmojisMap.put(0x1f301, R.drawable.emoji_1f301);
        sEmojisMap.put(0x1f308, R.drawable.emoji_1f308);
        sEmojisMap.put(0x1f30a, R.drawable.emoji_1f30a);

        // Objects
        sEmojisMap.put(0x1f38d, R.drawable.emoji_1f38d);
        sEmojisMap.put(0x1f49d, R.drawable.emoji_1f49d);
        sEmojisMap.put(0x1f38e, R.drawable.emoji_1f38e);
        sEmojisMap.put(0x1f392, R.drawable.emoji_1f392);
        sEmojisMap.put(0x1f393, R.drawable.emoji_1f393);
        sEmojisMap.put(0x1f38f, R.drawable.emoji_1f38f);
        sEmojisMap.put(0x1f386, R.drawable.emoji_1f386);
        sEmojisMap.put(0x1f387, R.drawable.emoji_1f387);
        sEmojisMap.put(0x1f390, R.drawable.emoji_1f390);
        sEmojisMap.put(0x1f391, R.drawable.emoji_1f391);
        sEmojisMap.put(0x1f383, R.drawable.emoji_1f383);
        sEmojisMap.put(0x1f47b, R.drawable.emoji_1f47b);
        sEmojisMap.put(0x1f385, R.drawable.emoji_1f385);
        sEmojisMap.put(0x1f384, R.drawable.emoji_1f384);
        sEmojisMap.put(0x1f381, R.drawable.emoji_1f381);
        sEmojisMap.put(0x1f38b, R.drawable.emoji_1f38b);
        sEmojisMap.put(0x1f389, R.drawable.emoji_1f389);
        sEmojisMap.put(0x1f38a, R.drawable.emoji_1f38a);
        sEmojisMap.put(0x1f388, R.drawable.emoji_1f388);
        sEmojisMap.put(0x1f38c, R.drawable.emoji_1f38c);
        sEmojisMap.put(0x1f52e, R.drawable.emoji_1f52e);
        sEmojisMap.put(0x1f3a5, R.drawable.emoji_1f3a5);
        sEmojisMap.put(0x1f4f7, R.drawable.emoji_1f4f7);
        sEmojisMap.put(0x1f4f9, R.drawable.emoji_1f4f9);
        sEmojisMap.put(0x1f4fc, R.drawable.emoji_1f4fc);
        sEmojisMap.put(0x1f4bf, R.drawable.emoji_1f4bf);
        sEmojisMap.put(0x1f4c0, R.drawable.emoji_1f4c0);
        sEmojisMap.put(0x1f4bd, R.drawable.emoji_1f4bd);
        sEmojisMap.put(0x1f4be, R.drawable.emoji_1f4be);
        sEmojisMap.put(0x1f4bb, R.drawable.emoji_1f4bb);
        sEmojisMap.put(0x1f4f1, R.drawable.emoji_1f4f1);
        sEmojisMap.put(0x260e, R.drawable.emoji_260e);
        sEmojisMap.put(0x1f4de, R.drawable.emoji_1f4de);
        sEmojisMap.put(0x1f4df, R.drawable.emoji_1f4df);
        sEmojisMap.put(0x1f4e0, R.drawable.emoji_1f4e0);
        sEmojisMap.put(0x1f4e1, R.drawable.emoji_1f4e1);
        sEmojisMap.put(0x1f4fa, R.drawable.emoji_1f4fa);
        sEmojisMap.put(0x1f4fb, R.drawable.emoji_1f4fb);
        sEmojisMap.put(0x1f50a, R.drawable.emoji_1f50a);
        sEmojisMap.put(0x1f509, R.drawable.emoji_1f509);
        sEmojisMap.put(0x1f508, R.drawable.emoji_1f508); // TODO (rockerhieu): review this emoji
        sEmojisMap.put(0x1f507, R.drawable.emoji_1f507);
        sEmojisMap.put(0x1f514, R.drawable.emoji_1f514);
        sEmojisMap.put(0x1f515, R.drawable.emoji_1f515);
        sEmojisMap.put(0x1f4e2, R.drawable.emoji_1f4e2);
        sEmojisMap.put(0x1f4e3, R.drawable.emoji_1f4e3);
        sEmojisMap.put(0x23f3, R.drawable.emoji_23f3);
        sEmojisMap.put(0x231b, R.drawable.emoji_231b);
        sEmojisMap.put(0x23f0, R.drawable.emoji_23f0);
        sEmojisMap.put(0x231a, R.drawable.emoji_231a);
        sEmojisMap.put(0x1f513, R.drawable.emoji_1f513);
        sEmojisMap.put(0x1f512, R.drawable.emoji_1f512);
        sEmojisMap.put(0x1f50f, R.drawable.emoji_1f50f);
        sEmojisMap.put(0x1f510, R.drawable.emoji_1f510);
        sEmojisMap.put(0x1f511, R.drawable.emoji_1f511);
        sEmojisMap.put(0x1f50e, R.drawable.emoji_1f50e);
        sEmojisMap.put(0x1f4a1, R.drawable.emoji_1f4a1);
        sEmojisMap.put(0x1f526, R.drawable.emoji_1f526);
        sEmojisMap.put(0x1f506, R.drawable.emoji_1f506);
        sEmojisMap.put(0x1f505, R.drawable.emoji_1f505);
        sEmojisMap.put(0x1f50c, R.drawable.emoji_1f50c);
        sEmojisMap.put(0x1f50b, R.drawable.emoji_1f50b);
        sEmojisMap.put(0x1f50d, R.drawable.emoji_1f50d);
        sEmojisMap.put(0x1f6c1, R.drawable.emoji_1f6c1);
        sEmojisMap.put(0x1f6c0, R.drawable.emoji_1f6c0);
        sEmojisMap.put(0x1f6bf, R.drawable.emoji_1f6bf);
        sEmojisMap.put(0x1f6bd, R.drawable.emoji_1f6bd);
        sEmojisMap.put(0x1f527, R.drawable.emoji_1f527);
        sEmojisMap.put(0x1f529, R.drawable.emoji_1f529);
        sEmojisMap.put(0x1f528, R.drawable.emoji_1f528);
        sEmojisMap.put(0x1f6aa, R.drawable.emoji_1f6aa);
        sEmojisMap.put(0x1f6ac, R.drawable.emoji_1f6ac);
        sEmojisMap.put(0x1f4a3, R.drawable.emoji_1f4a3);
        sEmojisMap.put(0x1f52b, R.drawable.emoji_1f52b);
        sEmojisMap.put(0x1f52a, R.drawable.emoji_1f52a);
        sEmojisMap.put(0x1f48a, R.drawable.emoji_1f48a);
        sEmojisMap.put(0x1f489, R.drawable.emoji_1f489);
        sEmojisMap.put(0x1f4b0, R.drawable.emoji_1f4b0);
        sEmojisMap.put(0x1f4b4, R.drawable.emoji_1f4b4);
        sEmojisMap.put(0x1f4b5, R.drawable.emoji_1f4b5);
        sEmojisMap.put(0x1f4b7, R.drawable.emoji_1f4b7);
        sEmojisMap.put(0x1f4b6, R.drawable.emoji_1f4b6);
        sEmojisMap.put(0x1f4b3, R.drawable.emoji_1f4b3);
        sEmojisMap.put(0x1f4b8, R.drawable.emoji_1f4b8);
        sEmojisMap.put(0x1f4f2, R.drawable.emoji_1f4f2);
        sEmojisMap.put(0x1f4e7, R.drawable.emoji_1f4e7);
        sEmojisMap.put(0x1f4e5, R.drawable.emoji_1f4e5);
        sEmojisMap.put(0x1f4e4, R.drawable.emoji_1f4e4);
        sEmojisMap.put(0x2709, R.drawable.emoji_2709);
        sEmojisMap.put(0x1f4e9, R.drawable.emoji_1f4e9);
        sEmojisMap.put(0x1f4e8, R.drawable.emoji_1f4e8);
        sEmojisMap.put(0x1f4ef, R.drawable.emoji_1f4ef);
        sEmojisMap.put(0x1f4eb, R.drawable.emoji_1f4eb);
        sEmojisMap.put(0x1f4ea, R.drawable.emoji_1f4ea);
        sEmojisMap.put(0x1f4ec, R.drawable.emoji_1f4ec);
        sEmojisMap.put(0x1f4ed, R.drawable.emoji_1f4ed);
        sEmojisMap.put(0x1f4ee, R.drawable.emoji_1f4ee);
        sEmojisMap.put(0x1f4e6, R.drawable.emoji_1f4e6);
        sEmojisMap.put(0x1f4dd, R.drawable.emoji_1f4dd);
        sEmojisMap.put(0x1f4c4, R.drawable.emoji_1f4c4);
        sEmojisMap.put(0x1f4c3, R.drawable.emoji_1f4c3);
        sEmojisMap.put(0x1f4d1, R.drawable.emoji_1f4d1);
        sEmojisMap.put(0x1f4ca, R.drawable.emoji_1f4ca);
        sEmojisMap.put(0x1f4c8, R.drawable.emoji_1f4c8);
        sEmojisMap.put(0x1f4c9, R.drawable.emoji_1f4c9);
        sEmojisMap.put(0x1f4dc, R.drawable.emoji_1f4dc);
        sEmojisMap.put(0x1f4cb, R.drawable.emoji_1f4cb);
        sEmojisMap.put(0x1f4c5, R.drawable.emoji_1f4c5);
        sEmojisMap.put(0x1f4c6, R.drawable.emoji_1f4c6);
        sEmojisMap.put(0x1f4c7, R.drawable.emoji_1f4c7);
        sEmojisMap.put(0x1f4c1, R.drawable.emoji_1f4c1);
        sEmojisMap.put(0x1f4c2, R.drawable.emoji_1f4c2);
        sEmojisMap.put(0x2702, R.drawable.emoji_2702);
        sEmojisMap.put(0x1f4cc, R.drawable.emoji_1f4cc);
        sEmojisMap.put(0x1f4ce, R.drawable.emoji_1f4ce);
        sEmojisMap.put(0x2712, R.drawable.emoji_2712);
        sEmojisMap.put(0x270f, R.drawable.emoji_270f);
        sEmojisMap.put(0x1f4cf, R.drawable.emoji_1f4cf);
        sEmojisMap.put(0x1f4d0, R.drawable.emoji_1f4d0);
        sEmojisMap.put(0x1f4d5, R.drawable.emoji_1f4d5);
        sEmojisMap.put(0x1f4d7, R.drawable.emoji_1f4d7);
        sEmojisMap.put(0x1f4d8, R.drawable.emoji_1f4d8);
        sEmojisMap.put(0x1f4d9, R.drawable.emoji_1f4d9);
        sEmojisMap.put(0x1f4d3, R.drawable.emoji_1f4d3);
        sEmojisMap.put(0x1f4d4, R.drawable.emoji_1f4d4);
        sEmojisMap.put(0x1f4d2, R.drawable.emoji_1f4d2);
        sEmojisMap.put(0x1f4da, R.drawable.emoji_1f4da);
        sEmojisMap.put(0x1f4d6, R.drawable.emoji_1f4d6);
        sEmojisMap.put(0x1f516, R.drawable.emoji_1f516);
        sEmojisMap.put(0x1f4db, R.drawable.emoji_1f4db);
        sEmojisMap.put(0x1f52c, R.drawable.emoji_1f52c);
        sEmojisMap.put(0x1f52d, R.drawable.emoji_1f52d);
        sEmojisMap.put(0x1f4f0, R.drawable.emoji_1f4f0);
        sEmojisMap.put(0x1f3a8, R.drawable.emoji_1f3a8);
        sEmojisMap.put(0x1f3ac, R.drawable.emoji_1f3ac);
        sEmojisMap.put(0x1f3a4, R.drawable.emoji_1f3a4);
        sEmojisMap.put(0x1f3a7, R.drawable.emoji_1f3a7);
        sEmojisMap.put(0x1f3bc, R.drawable.emoji_1f3bc);
        sEmojisMap.put(0x1f3b5, R.drawable.emoji_1f3b5);
        sEmojisMap.put(0x1f3b6, R.drawable.emoji_1f3b6);
        sEmojisMap.put(0x1f3b9, R.drawable.emoji_1f3b9);
        sEmojisMap.put(0x1f3bb, R.drawable.emoji_1f3bb);
        sEmojisMap.put(0x1f3ba, R.drawable.emoji_1f3ba);
        sEmojisMap.put(0x1f3b7, R.drawable.emoji_1f3b7);
        sEmojisMap.put(0x1f3b8, R.drawable.emoji_1f3b8);
        sEmojisMap.put(0x1f47e, R.drawable.emoji_1f47e);
        sEmojisMap.put(0x1f3ae, R.drawable.emoji_1f3ae);
        sEmojisMap.put(0x1f0cf, R.drawable.emoji_1f0cf);
        sEmojisMap.put(0x1f3b4, R.drawable.emoji_1f3b4);
        sEmojisMap.put(0x1f004, R.drawable.emoji_1f004);
        sEmojisMap.put(0x1f3b2, R.drawable.emoji_1f3b2);
        sEmojisMap.put(0x1f3af, R.drawable.emoji_1f3af);
        sEmojisMap.put(0x1f3c8, R.drawable.emoji_1f3c8);
        sEmojisMap.put(0x1f3c0, R.drawable.emoji_1f3c0);
        sEmojisMap.put(0x26bd, R.drawable.emoji_26bd);
        sEmojisMap.put(0x26be, R.drawable.emoji_26be);
        sEmojisMap.put(0x1f3be, R.drawable.emoji_1f3be);
        sEmojisMap.put(0x1f3b1, R.drawable.emoji_1f3b1);
        sEmojisMap.put(0x1f3c9, R.drawable.emoji_1f3c9);
        sEmojisMap.put(0x1f3b3, R.drawable.emoji_1f3b3);
        sEmojisMap.put(0x26f3, R.drawable.emoji_26f3);
        sEmojisMap.put(0x1f6b5, R.drawable.emoji_1f6b5);
        sEmojisMap.put(0x1f6b4, R.drawable.emoji_1f6b4);
        sEmojisMap.put(0x1f3c1, R.drawable.emoji_1f3c1);
        sEmojisMap.put(0x1f3c7, R.drawable.emoji_1f3c7);
        sEmojisMap.put(0x1f3c6, R.drawable.emoji_1f3c6);
        sEmojisMap.put(0x1f3bf, R.drawable.emoji_1f3bf);
        sEmojisMap.put(0x1f3c2, R.drawable.emoji_1f3c2);
        sEmojisMap.put(0x1f3ca, R.drawable.emoji_1f3ca);
        sEmojisMap.put(0x1f3c4, R.drawable.emoji_1f3c4);
        sEmojisMap.put(0x1f3a3, R.drawable.emoji_1f3a3);
        sEmojisMap.put(0x2615, R.drawable.emoji_2615);
        sEmojisMap.put(0x1f375, R.drawable.emoji_1f375);
        sEmojisMap.put(0x1f376, R.drawable.emoji_1f376);
        sEmojisMap.put(0x1f37c, R.drawable.emoji_1f37c);
        sEmojisMap.put(0x1f37a, R.drawable.emoji_1f37a);
        sEmojisMap.put(0x1f37b, R.drawable.emoji_1f37b);
        sEmojisMap.put(0x1f378, R.drawable.emoji_1f378);
        sEmojisMap.put(0x1f379, R.drawable.emoji_1f379);
        sEmojisMap.put(0x1f377, R.drawable.emoji_1f377);
        sEmojisMap.put(0x1f374, R.drawable.emoji_1f374);
        sEmojisMap.put(0x1f355, R.drawable.emoji_1f355);
        sEmojisMap.put(0x1f354, R.drawable.emoji_1f354);
        sEmojisMap.put(0x1f35f, R.drawable.emoji_1f35f);
        sEmojisMap.put(0x1f357, R.drawable.emoji_1f357);
        sEmojisMap.put(0x1f356, R.drawable.emoji_1f356);
        sEmojisMap.put(0x1f35d, R.drawable.emoji_1f35d);
        sEmojisMap.put(0x1f35b, R.drawable.emoji_1f35b);
        sEmojisMap.put(0x1f364, R.drawable.emoji_1f364);
        sEmojisMap.put(0x1f371, R.drawable.emoji_1f371);
        sEmojisMap.put(0x1f363, R.drawable.emoji_1f363);
        sEmojisMap.put(0x1f365, R.drawable.emoji_1f365);
        sEmojisMap.put(0x1f359, R.drawable.emoji_1f359);
        sEmojisMap.put(0x1f358, R.drawable.emoji_1f358);
        sEmojisMap.put(0x1f35a, R.drawable.emoji_1f35a);
        sEmojisMap.put(0x1f35c, R.drawable.emoji_1f35c);
        sEmojisMap.put(0x1f372, R.drawable.emoji_1f372);
        sEmojisMap.put(0x1f362, R.drawable.emoji_1f362);
        sEmojisMap.put(0x1f361, R.drawable.emoji_1f361);
        sEmojisMap.put(0x1f373, R.drawable.emoji_1f373);
        sEmojisMap.put(0x1f35e, R.drawable.emoji_1f35e);
        sEmojisMap.put(0x1f369, R.drawable.emoji_1f369);
        sEmojisMap.put(0x1f36e, R.drawable.emoji_1f36e);
        sEmojisMap.put(0x1f366, R.drawable.emoji_1f366);
        sEmojisMap.put(0x1f368, R.drawable.emoji_1f368);
        sEmojisMap.put(0x1f367, R.drawable.emoji_1f367);
        sEmojisMap.put(0x1f382, R.drawable.emoji_1f382);
        sEmojisMap.put(0x1f370, R.drawable.emoji_1f370);
        sEmojisMap.put(0x1f36a, R.drawable.emoji_1f36a);
        sEmojisMap.put(0x1f36b, R.drawable.emoji_1f36b);
        sEmojisMap.put(0x1f36c, R.drawable.emoji_1f36c);
        sEmojisMap.put(0x1f36d, R.drawable.emoji_1f36d);
        sEmojisMap.put(0x1f36f, R.drawable.emoji_1f36f);
        sEmojisMap.put(0x1f34e, R.drawable.emoji_1f34e);
        sEmojisMap.put(0x1f34f, R.drawable.emoji_1f34f);
        sEmojisMap.put(0x1f34a, R.drawable.emoji_1f34a);
        sEmojisMap.put(0x1f34b, R.drawable.emoji_1f34b);
        sEmojisMap.put(0x1f352, R.drawable.emoji_1f352);
        sEmojisMap.put(0x1f347, R.drawable.emoji_1f347);
        sEmojisMap.put(0x1f349, R.drawable.emoji_1f349);
        sEmojisMap.put(0x1f353, R.drawable.emoji_1f353);
        sEmojisMap.put(0x1f351, R.drawable.emoji_1f351);
        sEmojisMap.put(0x1f348, R.drawable.emoji_1f348);
        sEmojisMap.put(0x1f34c, R.drawable.emoji_1f34c);
        sEmojisMap.put(0x1f350, R.drawable.emoji_1f350);
        sEmojisMap.put(0x1f34d, R.drawable.emoji_1f34d);
        sEmojisMap.put(0x1f360, R.drawable.emoji_1f360);
        sEmojisMap.put(0x1f346, R.drawable.emoji_1f346);
        sEmojisMap.put(0x1f345, R.drawable.emoji_1f345);
        sEmojisMap.put(0x1f33d, R.drawable.emoji_1f33d);

        // Places
        sEmojisMap.put(0x1f3e0, R.drawable.emoji_1f3e0);
        sEmojisMap.put(0x1f3e1, R.drawable.emoji_1f3e1);
        sEmojisMap.put(0x1f3eb, R.drawable.emoji_1f3eb);
        sEmojisMap.put(0x1f3e2, R.drawable.emoji_1f3e2);
        sEmojisMap.put(0x1f3e3, R.drawable.emoji_1f3e3);
        sEmojisMap.put(0x1f3e5, R.drawable.emoji_1f3e5);
        sEmojisMap.put(0x1f3e6, R.drawable.emoji_1f3e6);
        sEmojisMap.put(0x1f3ea, R.drawable.emoji_1f3ea);
        sEmojisMap.put(0x1f3e9, R.drawable.emoji_1f3e9);
        sEmojisMap.put(0x1f3e8, R.drawable.emoji_1f3e8);
        sEmojisMap.put(0x1f492, R.drawable.emoji_1f492);
        sEmojisMap.put(0x26ea, R.drawable.emoji_26ea);
        sEmojisMap.put(0x1f3ec, R.drawable.emoji_1f3ec);
        sEmojisMap.put(0x1f3e4, R.drawable.emoji_1f3e4);
        sEmojisMap.put(0x1f307, R.drawable.emoji_1f307);
        sEmojisMap.put(0x1f306, R.drawable.emoji_1f306);
        sEmojisMap.put(0x1f3ef, R.drawable.emoji_1f3ef);
        sEmojisMap.put(0x1f3f0, R.drawable.emoji_1f3f0);
        sEmojisMap.put(0x26fa, R.drawable.emoji_26fa);
        sEmojisMap.put(0x1f3ed, R.drawable.emoji_1f3ed);
        sEmojisMap.put(0x1f5fc, R.drawable.emoji_1f5fc);
        sEmojisMap.put(0x1f5fe, R.drawable.emoji_1f5fe);
        sEmojisMap.put(0x1f5fb, R.drawable.emoji_1f5fb);
        sEmojisMap.put(0x1f304, R.drawable.emoji_1f304);
        sEmojisMap.put(0x1f305, R.drawable.emoji_1f305);
        sEmojisMap.put(0x1f303, R.drawable.emoji_1f303);
        sEmojisMap.put(0x1f5fd, R.drawable.emoji_1f5fd);
        sEmojisMap.put(0x1f309, R.drawable.emoji_1f309);
        sEmojisMap.put(0x1f3a0, R.drawable.emoji_1f3a0);
        sEmojisMap.put(0x1f3a1, R.drawable.emoji_1f3a1);
        sEmojisMap.put(0x26f2, R.drawable.emoji_26f2);
        sEmojisMap.put(0x1f3a2, R.drawable.emoji_1f3a2);
        sEmojisMap.put(0x1f6a2, R.drawable.emoji_1f6a2);
        sEmojisMap.put(0x26f5, R.drawable.emoji_26f5);
        sEmojisMap.put(0x1f6a4, R.drawable.emoji_1f6a4);
        sEmojisMap.put(0x1f6a3, R.drawable.emoji_1f6a3);
        sEmojisMap.put(0x2693, R.drawable.emoji_2693);
        sEmojisMap.put(0x1f680, R.drawable.emoji_1f680);
        sEmojisMap.put(0x2708, R.drawable.emoji_2708);
        sEmojisMap.put(0x1f4ba, R.drawable.emoji_1f4ba);
        sEmojisMap.put(0x1f681, R.drawable.emoji_1f681);
        sEmojisMap.put(0x1f682, R.drawable.emoji_1f682);
        sEmojisMap.put(0x1f68a, R.drawable.emoji_1f68a);
        sEmojisMap.put(0x1f689, R.drawable.emoji_1f689);
        sEmojisMap.put(0x1f69e, R.drawable.emoji_1f69e);
        sEmojisMap.put(0x1f686, R.drawable.emoji_1f686);
        sEmojisMap.put(0x1f684, R.drawable.emoji_1f684);
        sEmojisMap.put(0x1f685, R.drawable.emoji_1f685);
        sEmojisMap.put(0x1f688, R.drawable.emoji_1f688);
        sEmojisMap.put(0x1f687, R.drawable.emoji_1f687);
        sEmojisMap.put(0x1f69d, R.drawable.emoji_1f69d);
        sEmojisMap.put(0x1f68b, R.drawable.emoji_1f68b); // TODO (rockerhieu) review this emoji
        sEmojisMap.put(0x1f683, R.drawable.emoji_1f683);
        sEmojisMap.put(0x1f68e, R.drawable.emoji_1f68e);
        sEmojisMap.put(0x1f68c, R.drawable.emoji_1f68c);
        sEmojisMap.put(0x1f68d, R.drawable.emoji_1f68d);
        sEmojisMap.put(0x1f699, R.drawable.emoji_1f699);
        sEmojisMap.put(0x1f698, R.drawable.emoji_1f698);
        sEmojisMap.put(0x1f697, R.drawable.emoji_1f697);
        sEmojisMap.put(0x1f695, R.drawable.emoji_1f695);
        sEmojisMap.put(0x1f696, R.drawable.emoji_1f696);
        sEmojisMap.put(0x1f69b, R.drawable.emoji_1f69b);
        sEmojisMap.put(0x1f69a, R.drawable.emoji_1f69a);
        sEmojisMap.put(0x1f6a8, R.drawable.emoji_1f6a8);
        sEmojisMap.put(0x1f693, R.drawable.emoji_1f693);
        sEmojisMap.put(0x1f694, R.drawable.emoji_1f694);
        sEmojisMap.put(0x1f692, R.drawable.emoji_1f692);
        sEmojisMap.put(0x1f691, R.drawable.emoji_1f691);
        sEmojisMap.put(0x1f690, R.drawable.emoji_1f690);
        sEmojisMap.put(0x1f6b2, R.drawable.emoji_1f6b2);
        sEmojisMap.put(0x1f6a1, R.drawable.emoji_1f6a1);
        sEmojisMap.put(0x1f69f, R.drawable.emoji_1f69f);
        sEmojisMap.put(0x1f6a0, R.drawable.emoji_1f6a0);
        sEmojisMap.put(0x1f69c, R.drawable.emoji_1f69c);
        sEmojisMap.put(0x1f488, R.drawable.emoji_1f488);
        sEmojisMap.put(0x1f68f, R.drawable.emoji_1f68f);
        sEmojisMap.put(0x1f3ab, R.drawable.emoji_1f3ab);
        sEmojisMap.put(0x1f6a6, R.drawable.emoji_1f6a6);
        sEmojisMap.put(0x1f6a5, R.drawable.emoji_1f6a5);
        sEmojisMap.put(0x26a0, R.drawable.emoji_26a0);
        sEmojisMap.put(0x1f6a7, R.drawable.emoji_1f6a7);
        sEmojisMap.put(0x1f530, R.drawable.emoji_1f530);
        sEmojisMap.put(0x26fd, R.drawable.emoji_26fd);
        sEmojisMap.put(0x1f3ee, R.drawable.emoji_1f3ee);
        sEmojisMap.put(0x1f3b0, R.drawable.emoji_1f3b0);
        sEmojisMap.put(0x2668, R.drawable.emoji_2668);
        sEmojisMap.put(0x1f5ff, R.drawable.emoji_1f5ff);
        sEmojisMap.put(0x1f3aa, R.drawable.emoji_1f3aa);
        sEmojisMap.put(0x1f3ad, R.drawable.emoji_1f3ad);
        sEmojisMap.put(0x1f4cd, R.drawable.emoji_1f4cd);
        sEmojisMap.put(0x1f6a9, R.drawable.emoji_1f6a9);
//        Emoji.fromChars("\ud83c\uddef\ud83c\uddf5");
//        Emoji.fromChars("\ud83c\uddf0\ud83c\uddf7");
//        Emoji.fromChars("\ud83c\udde9\ud83c\uddea");
//        Emoji.fromChars("\ud83c\udde8\ud83c\uddf3");
//        Emoji.fromChars("\ud83c\uddfa\ud83c\uddf8");
//        Emoji.fromChars("\ud83c\uddeb\ud83c\uddf7");
//        Emoji.fromChars("\ud83c\uddea\ud83c\uddf8");
//        Emoji.fromChars("\ud83c\uddee\ud83c\uddf9");
//        Emoji.fromChars("\ud83c\uddf7\ud83c\uddfa");
//        Emoji.fromChars("\ud83c\uddec\ud83c\udde7");

        // Symbols
//        Emoji.fromChars("\u0031\u20e3"),
//        Emoji.fromChars("\u0032\u20e3"),
//        Emoji.fromChars("\u0033\u20e3"),
//        Emoji.fromChars("\u0034\u20e3"),
//        Emoji.fromChars("\u0035\u20e3"),
//        Emoji.fromChars("\u0036\u20e3"),
//        Emoji.fromChars("\u0037\u20e3"),
//        Emoji.fromChars("\u0038\u20e3"),
//        Emoji.fromChars("\u0039\u20e3"),
//        Emoji.fromChars("\u0030\u20e3"),
        sEmojisMap.put(0x1f51f, R.drawable.emoji_1f51f);
        sEmojisMap.put(0x1f522, R.drawable.emoji_1f522);
//        Emoji.fromChars("\u0023\u20e3"),
        sEmojisMap.put(0x1f523, R.drawable.emoji_1f523);
        sEmojisMap.put(0x2b06, R.drawable.emoji_2b06);
        sEmojisMap.put(0x2b07, R.drawable.emoji_2b07);
        sEmojisMap.put(0x2b05, R.drawable.emoji_2b05);
        sEmojisMap.put(0x27a1, R.drawable.emoji_27a1);
        sEmojisMap.put(0x1f520, R.drawable.emoji_1f520);
        sEmojisMap.put(0x1f521, R.drawable.emoji_1f521);
        sEmojisMap.put(0x1f524, R.drawable.emoji_1f524);
        sEmojisMap.put(0x2197, R.drawable.emoji_2197);
        sEmojisMap.put(0x2196, R.drawable.emoji_2196);
        sEmojisMap.put(0x2198, R.drawable.emoji_2198);
        sEmojisMap.put(0x2199, R.drawable.emoji_2199);
        sEmojisMap.put(0x2194, R.drawable.emoji_2194);
        sEmojisMap.put(0x2195, R.drawable.emoji_2195);
        sEmojisMap.put(0x1f504, R.drawable.emoji_1f504);
        sEmojisMap.put(0x25c0, R.drawable.emoji_25c0);
        sEmojisMap.put(0x25b6, R.drawable.emoji_25b6);
        sEmojisMap.put(0x1f53c, R.drawable.emoji_1f53c);
        sEmojisMap.put(0x1f53d, R.drawable.emoji_1f53d);
        sEmojisMap.put(0x21a9, R.drawable.emoji_21a9);
        sEmojisMap.put(0x21aa, R.drawable.emoji_21aa);
        sEmojisMap.put(0x2139, R.drawable.emoji_2139);
        sEmojisMap.put(0x23ea, R.drawable.emoji_23ea);
        sEmojisMap.put(0x23e9, R.drawable.emoji_23e9);
        sEmojisMap.put(0x23eb, R.drawable.emoji_23eb);
        sEmojisMap.put(0x23ec, R.drawable.emoji_23ec);
        sEmojisMap.put(0x2935, R.drawable.emoji_2935);
        sEmojisMap.put(0x2934, R.drawable.emoji_2934);
        sEmojisMap.put(0x1f197, R.drawable.emoji_1f197);
        sEmojisMap.put(0x1f500, R.drawable.emoji_1f500);
        sEmojisMap.put(0x1f501, R.drawable.emoji_1f501);
        sEmojisMap.put(0x1f502, R.drawable.emoji_1f502);
        sEmojisMap.put(0x1f195, R.drawable.emoji_1f195);
        sEmojisMap.put(0x1f199, R.drawable.emoji_1f199);
        sEmojisMap.put(0x1f192, R.drawable.emoji_1f192);
        sEmojisMap.put(0x1f193, R.drawable.emoji_1f193);
        sEmojisMap.put(0x1f196, R.drawable.emoji_1f196);
        sEmojisMap.put(0x1f4f6, R.drawable.emoji_1f4f6);
        sEmojisMap.put(0x1f3a6, R.drawable.emoji_1f3a6);
        sEmojisMap.put(0x1f201, R.drawable.emoji_1f201);
        sEmojisMap.put(0x1f22f, R.drawable.emoji_1f22f);
        sEmojisMap.put(0x1f233, R.drawable.emoji_1f233);
        sEmojisMap.put(0x1f235, R.drawable.emoji_1f235);
        sEmojisMap.put(0x1f234, R.drawable.emoji_1f234);
        sEmojisMap.put(0x1f232, R.drawable.emoji_1f232);
        sEmojisMap.put(0x1f250, R.drawable.emoji_1f250);
        sEmojisMap.put(0x1f239, R.drawable.emoji_1f239);
        sEmojisMap.put(0x1f23a, R.drawable.emoji_1f23a);
        sEmojisMap.put(0x1f236, R.drawable.emoji_1f236);
        sEmojisMap.put(0x1f21a, R.drawable.emoji_1f21a);
        sEmojisMap.put(0x1f6bb, R.drawable.emoji_1f6bb);
        sEmojisMap.put(0x1f6b9, R.drawable.emoji_1f6b9);
        sEmojisMap.put(0x1f6ba, R.drawable.emoji_1f6ba);
        sEmojisMap.put(0x1f6bc, R.drawable.emoji_1f6bc);
        sEmojisMap.put(0x1f6be, R.drawable.emoji_1f6be);
        sEmojisMap.put(0x1f6b0, R.drawable.emoji_1f6b0);
        sEmojisMap.put(0x1f6ae, R.drawable.emoji_1f6ae);
        sEmojisMap.put(0x1f17f, R.drawable.emoji_1f17f);
        sEmojisMap.put(0x267f, R.drawable.emoji_267f);
        sEmojisMap.put(0x1f6ad, R.drawable.emoji_1f6ad);
        sEmojisMap.put(0x1f237, R.drawable.emoji_1f237);
        sEmojisMap.put(0x1f238, R.drawable.emoji_1f238);
        sEmojisMap.put(0x1f202, R.drawable.emoji_1f202);
        sEmojisMap.put(0x24c2, R.drawable.emoji_24c2);
        sEmojisMap.put(0x1f6c2, R.drawable.emoji_1f6c2);
        sEmojisMap.put(0x1f6c4, R.drawable.emoji_1f6c4);
        sEmojisMap.put(0x1f6c5, R.drawable.emoji_1f6c5);
        sEmojisMap.put(0x1f6c3, R.drawable.emoji_1f6c3);
        sEmojisMap.put(0x1f251, R.drawable.emoji_1f251);
        sEmojisMap.put(0x3299, R.drawable.emoji_3299);
        sEmojisMap.put(0x3297, R.drawable.emoji_3297);
        sEmojisMap.put(0x1f191, R.drawable.emoji_1f191);
        sEmojisMap.put(0x1f198, R.drawable.emoji_1f198);
        sEmojisMap.put(0x1f194, R.drawable.emoji_1f194);
        sEmojisMap.put(0x1f6ab, R.drawable.emoji_1f6ab);
        sEmojisMap.put(0x1f51e, R.drawable.emoji_1f51e);
        sEmojisMap.put(0x1f4f5, R.drawable.emoji_1f4f5);
        sEmojisMap.put(0x1f6af, R.drawable.emoji_1f6af);
        sEmojisMap.put(0x1f6b1, R.drawable.emoji_1f6b1);
        sEmojisMap.put(0x1f6b3, R.drawable.emoji_1f6b3);
        sEmojisMap.put(0x1f6b7, R.drawable.emoji_1f6b7);
        sEmojisMap.put(0x1f6b8, R.drawable.emoji_1f6b8);
        sEmojisMap.put(0x26d4, R.drawable.emoji_26d4);
        sEmojisMap.put(0x2733, R.drawable.emoji_2733);
        sEmojisMap.put(0x2747, R.drawable.emoji_2747);
        sEmojisMap.put(0x274e, R.drawable.emoji_274e);
        sEmojisMap.put(0x2705, R.drawable.emoji_2705);
        sEmojisMap.put(0x2734, R.drawable.emoji_2734);
        sEmojisMap.put(0x1f49f, R.drawable.emoji_1f49f);
        sEmojisMap.put(0x1f19a, R.drawable.emoji_1f19a);
        sEmojisMap.put(0x1f4f3, R.drawable.emoji_1f4f3);
        sEmojisMap.put(0x1f4f4, R.drawable.emoji_1f4f4);
        sEmojisMap.put(0x1f170, R.drawable.emoji_1f170);
        sEmojisMap.put(0x1f171, R.drawable.emoji_1f171);
        sEmojisMap.put(0x1f18e, R.drawable.emoji_1f18e);
        sEmojisMap.put(0x1f17e, R.drawable.emoji_1f17e);
        sEmojisMap.put(0x1f4a0, R.drawable.emoji_1f4a0);
        sEmojisMap.put(0x27bf, R.drawable.emoji_27bf);
        sEmojisMap.put(0x267b, R.drawable.emoji_267b);
        sEmojisMap.put(0x2648, R.drawable.emoji_2648);
        sEmojisMap.put(0x2649, R.drawable.emoji_2649);
        sEmojisMap.put(0x264a, R.drawable.emoji_264a);
        sEmojisMap.put(0x264b, R.drawable.emoji_264b);
        sEmojisMap.put(0x264c, R.drawable.emoji_264c);
        sEmojisMap.put(0x264d, R.drawable.emoji_264d);
        sEmojisMap.put(0x264e, R.drawable.emoji_264e);
        sEmojisMap.put(0x264f, R.drawable.emoji_264f);
        sEmojisMap.put(0x2650, R.drawable.emoji_2650);
        sEmojisMap.put(0x2651, R.drawable.emoji_2651);
        sEmojisMap.put(0x2652, R.drawable.emoji_2652);
        sEmojisMap.put(0x2653, R.drawable.emoji_2653);
        sEmojisMap.put(0x26ce, R.drawable.emoji_26ce);
        sEmojisMap.put(0x1f52f, R.drawable.emoji_1f52f);
        sEmojisMap.put(0x1f3e7, R.drawable.emoji_1f3e7);
        sEmojisMap.put(0x1f4b9, R.drawable.emoji_1f4b9);
        sEmojisMap.put(0x1f4b2, R.drawable.emoji_1f4b2);
        sEmojisMap.put(0x1f4b1, R.drawable.emoji_1f4b1);
        sEmojisMap.put(0x00a9, R.drawable.emoji_00a9);
        sEmojisMap.put(0x00ae, R.drawable.emoji_00ae);
        sEmojisMap.put(0x2122, R.drawable.emoji_2122);
        sEmojisMap.put(0x274c, R.drawable.emoji_274c);
        sEmojisMap.put(0x203c, R.drawable.emoji_203c);
        sEmojisMap.put(0x2049, R.drawable.emoji_2049);
        sEmojisMap.put(0x2757, R.drawable.emoji_2757);
        sEmojisMap.put(0x2753, R.drawable.emoji_2753);
        sEmojisMap.put(0x2755, R.drawable.emoji_2755);
        sEmojisMap.put(0x2754, R.drawable.emoji_2754);
        sEmojisMap.put(0x2b55, R.drawable.emoji_2b55);
        sEmojisMap.put(0x1f51d, R.drawable.emoji_1f51d);
        sEmojisMap.put(0x1f51a, R.drawable.emoji_1f51a);
        sEmojisMap.put(0x1f519, R.drawable.emoji_1f519);
        sEmojisMap.put(0x1f51b, R.drawable.emoji_1f51b);
        sEmojisMap.put(0x1f51c, R.drawable.emoji_1f51c);
        sEmojisMap.put(0x1f503, R.drawable.emoji_1f503);
        sEmojisMap.put(0x1f55b, R.drawable.emoji_1f55b);
        sEmojisMap.put(0x1f567, R.drawable.emoji_1f567);
        sEmojisMap.put(0x1f550, R.drawable.emoji_1f550);
        sEmojisMap.put(0x1f55c, R.drawable.emoji_1f55c);
        sEmojisMap.put(0x1f551, R.drawable.emoji_1f551);
        sEmojisMap.put(0x1f55d, R.drawable.emoji_1f55d);
        sEmojisMap.put(0x1f552, R.drawable.emoji_1f552);
        sEmojisMap.put(0x1f55e, R.drawable.emoji_1f55e);
        sEmojisMap.put(0x1f553, R.drawable.emoji_1f553);
        sEmojisMap.put(0x1f55f, R.drawable.emoji_1f55f);
        sEmojisMap.put(0x1f554, R.drawable.emoji_1f554);
        sEmojisMap.put(0x1f560, R.drawable.emoji_1f560);
        sEmojisMap.put(0x1f555, R.drawable.emoji_1f555);
        sEmojisMap.put(0x1f556, R.drawable.emoji_1f556);
        sEmojisMap.put(0x1f557, R.drawable.emoji_1f557);
        sEmojisMap.put(0x1f558, R.drawable.emoji_1f558);
        sEmojisMap.put(0x1f559, R.drawable.emoji_1f559);
        sEmojisMap.put(0x1f55a, R.drawable.emoji_1f55a);
        sEmojisMap.put(0x1f561, R.drawable.emoji_1f561);
        sEmojisMap.put(0x1f562, R.drawable.emoji_1f562);
        sEmojisMap.put(0x1f563, R.drawable.emoji_1f563);
        sEmojisMap.put(0x1f564, R.drawable.emoji_1f564);
        sEmojisMap.put(0x1f565, R.drawable.emoji_1f565);
        sEmojisMap.put(0x1f566, R.drawable.emoji_1f566);
        sEmojisMap.put(0x2716, R.drawable.emoji_2716);
        sEmojisMap.put(0x2795, R.drawable.emoji_2795);
        sEmojisMap.put(0x2796, R.drawable.emoji_2796);
        sEmojisMap.put(0x2797, R.drawable.emoji_2797);
        sEmojisMap.put(0x2660, R.drawable.emoji_2660);
        sEmojisMap.put(0x2665, R.drawable.emoji_2665);
        sEmojisMap.put(0x2663, R.drawable.emoji_2663);
        sEmojisMap.put(0x2666, R.drawable.emoji_2666);
        sEmojisMap.put(0x1f4ae, R.drawable.emoji_1f4ae);
        sEmojisMap.put(0x1f4af, R.drawable.emoji_1f4af);
        sEmojisMap.put(0x2714, R.drawable.emoji_2714);
        sEmojisMap.put(0x2611, R.drawable.emoji_2611);
        sEmojisMap.put(0x1f518, R.drawable.emoji_1f518);
        sEmojisMap.put(0x1f517, R.drawable.emoji_1f517);
        sEmojisMap.put(0x27b0, R.drawable.emoji_27b0);
        sEmojisMap.put(0x3030, R.drawable.emoji_3030);
        sEmojisMap.put(0x303d, R.drawable.emoji_303d);
        sEmojisMap.put(0x1f531, R.drawable.emoji_1f531);
        sEmojisMap.put(0x25fc, R.drawable.emoji_25fc);
        sEmojisMap.put(0x25fb, R.drawable.emoji_25fb);
        sEmojisMap.put(0x25fe, R.drawable.emoji_25fe);
        sEmojisMap.put(0x25fd, R.drawable.emoji_25fd);
        sEmojisMap.put(0x25aa, R.drawable.emoji_25aa);
        sEmojisMap.put(0x25ab, R.drawable.emoji_25ab);
        sEmojisMap.put(0x1f53a, R.drawable.emoji_1f53a);
        sEmojisMap.put(0x1f532, R.drawable.emoji_1f532);
        sEmojisMap.put(0x1f533, R.drawable.emoji_1f533);
        sEmojisMap.put(0x26ab, R.drawable.emoji_26ab);
        sEmojisMap.put(0x26aa, R.drawable.emoji_26aa);
        sEmojisMap.put(0x1f534, R.drawable.emoji_1f534);
        sEmojisMap.put(0x1f535, R.drawable.emoji_1f535);
        sEmojisMap.put(0x1f53b, R.drawable.emoji_1f53b);
        sEmojisMap.put(0x2b1c, R.drawable.emoji_2b1c);
        sEmojisMap.put(0x2b1b, R.drawable.emoji_2b1b);
        sEmojisMap.put(0x1f536, R.drawable.emoji_1f536);
        sEmojisMap.put(0x1f537, R.drawable.emoji_1f537);
        sEmojisMap.put(0x1f538, R.drawable.emoji_1f538);
        sEmojisMap.put(0x1f539, R.drawable.emoji_1f539);


        sSoftbanksMap.put(0xe001, R.drawable.emoji_1f466);
        sSoftbanksMap.put(0xe002, R.drawable.emoji_1f467);
        sSoftbanksMap.put(0xe003, R.drawable.emoji_1f48b);
        sSoftbanksMap.put(0xe004, R.drawable.emoji_1f468);
        sSoftbanksMap.put(0xe005, R.drawable.emoji_1f469);
        sSoftbanksMap.put(0xe006, R.drawable.emoji_1f455);
        sSoftbanksMap.put(0xe007, R.drawable.emoji_1f45e);
        sSoftbanksMap.put(0xe008, R.drawable.emoji_1f4f7);
        sSoftbanksMap.put(0xe009, R.drawable.emoji_1f4de);
        sSoftbanksMap.put(0xe00a, R.drawable.emoji_1f4f1);
        sSoftbanksMap.put(0xe00b, R.drawable.emoji_1f4e0);
        sSoftbanksMap.put(0xe00c, R.drawable.emoji_1f4bb);
        sSoftbanksMap.put(0xe00d, R.drawable.emoji_1f44a);
        sSoftbanksMap.put(0xe00e, R.drawable.emoji_1f44d);
        sSoftbanksMap.put(0xe00f, R.drawable.emoji_261d);
        sSoftbanksMap.put(0xe010, R.drawable.emoji_270a);
        sSoftbanksMap.put(0xe011, R.drawable.emoji_270c);
        sSoftbanksMap.put(0xe012, R.drawable.emoji_1f64b);
        sSoftbanksMap.put(0xe013, R.drawable.emoji_1f3bf);
        sSoftbanksMap.put(0xe014, R.drawable.emoji_26f3);
        sSoftbanksMap.put(0xe015, R.drawable.emoji_1f3be);
        sSoftbanksMap.put(0xe016, R.drawable.emoji_26be);
        sSoftbanksMap.put(0xe017, R.drawable.emoji_1f3c4);
        sSoftbanksMap.put(0xe018, R.drawable.emoji_26bd);
        sSoftbanksMap.put(0xe019, R.drawable.emoji_1f3a3);
        sSoftbanksMap.put(0xe01a, R.drawable.emoji_1f434);
        sSoftbanksMap.put(0xe01b, R.drawable.emoji_1f697);
        sSoftbanksMap.put(0xe01c, R.drawable.emoji_26f5);
        sSoftbanksMap.put(0xe01d, R.drawable.emoji_2708);
        sSoftbanksMap.put(0xe01e, R.drawable.emoji_1f683);
        sSoftbanksMap.put(0xe01f, R.drawable.emoji_1f685);
        sSoftbanksMap.put(0xe020, R.drawable.emoji_2753);
        sSoftbanksMap.put(0xe021, R.drawable.emoji_2757);
        sSoftbanksMap.put(0xe022, R.drawable.emoji_2764);
        sSoftbanksMap.put(0xe023, R.drawable.emoji_1f494);
        sSoftbanksMap.put(0xe024, R.drawable.emoji_1f550);
        sSoftbanksMap.put(0xe025, R.drawable.emoji_1f551);
        sSoftbanksMap.put(0xe026, R.drawable.emoji_1f552);
        sSoftbanksMap.put(0xe027, R.drawable.emoji_1f553);
        sSoftbanksMap.put(0xe028, R.drawable.emoji_1f554);
        sSoftbanksMap.put(0xe029, R.drawable.emoji_1f555);
        sSoftbanksMap.put(0xe02a, R.drawable.emoji_1f556);
        sSoftbanksMap.put(0xe02b, R.drawable.emoji_1f557);
        sSoftbanksMap.put(0xe02c, R.drawable.emoji_1f558);
        sSoftbanksMap.put(0xe02d, R.drawable.emoji_1f559);
        sSoftbanksMap.put(0xe02e, R.drawable.emoji_1f55a);
        sSoftbanksMap.put(0xe02f, R.drawable.emoji_1f55b);
        sSoftbanksMap.put(0xe030, R.drawable.emoji_1f338);
        sSoftbanksMap.put(0xe031, R.drawable.emoji_1f531);
        sSoftbanksMap.put(0xe032, R.drawable.emoji_1f339);
        sSoftbanksMap.put(0xe033, R.drawable.emoji_1f384);
        sSoftbanksMap.put(0xe034, R.drawable.emoji_1f48d);
        sSoftbanksMap.put(0xe035, R.drawable.emoji_1f48e);
        sSoftbanksMap.put(0xe036, R.drawable.emoji_1f3e0);
        sSoftbanksMap.put(0xe037, R.drawable.emoji_26ea);
        sSoftbanksMap.put(0xe038, R.drawable.emoji_1f3e2);
        sSoftbanksMap.put(0xe039, R.drawable.emoji_1f689);
        sSoftbanksMap.put(0xe03a, R.drawable.emoji_26fd);
        sSoftbanksMap.put(0xe03b, R.drawable.emoji_1f5fb);
        sSoftbanksMap.put(0xe03c, R.drawable.emoji_1f3a4);
        sSoftbanksMap.put(0xe03d, R.drawable.emoji_1f3a5);
        sSoftbanksMap.put(0xe03e, R.drawable.emoji_1f3b5);
        sSoftbanksMap.put(0xe03f, R.drawable.emoji_1f511);
        sSoftbanksMap.put(0xe040, R.drawable.emoji_1f3b7);
        sSoftbanksMap.put(0xe041, R.drawable.emoji_1f3b8);
        sSoftbanksMap.put(0xe042, R.drawable.emoji_1f3ba);
        sSoftbanksMap.put(0xe043, R.drawable.emoji_1f374);
        sSoftbanksMap.put(0xe044, R.drawable.emoji_1f377);
        sSoftbanksMap.put(0xe045, R.drawable.emoji_2615);
        sSoftbanksMap.put(0xe046, R.drawable.emoji_1f370);
        sSoftbanksMap.put(0xe047, R.drawable.emoji_1f37a);
        sSoftbanksMap.put(0xe048, R.drawable.emoji_26c4);
        sSoftbanksMap.put(0xe049, R.drawable.emoji_2601);
        sSoftbanksMap.put(0xe04a, R.drawable.emoji_2600);
        sSoftbanksMap.put(0xe04b, R.drawable.emoji_2614);
        sSoftbanksMap.put(0xe04c, R.drawable.emoji_1f313);
        sSoftbanksMap.put(0xe04d, R.drawable.emoji_1f304);
        sSoftbanksMap.put(0xe04e, R.drawable.emoji_1f47c);
        sSoftbanksMap.put(0xe04f, R.drawable.emoji_1f431);
        sSoftbanksMap.put(0xe050, R.drawable.emoji_1f42f);
        sSoftbanksMap.put(0xe051, R.drawable.emoji_1f43b);
        sSoftbanksMap.put(0xe052, R.drawable.emoji_1f429);
        sSoftbanksMap.put(0xe053, R.drawable.emoji_1f42d);
        sSoftbanksMap.put(0xe054, R.drawable.emoji_1f433);
        sSoftbanksMap.put(0xe055, R.drawable.emoji_1f427);
        sSoftbanksMap.put(0xe056, R.drawable.emoji_1f60a);
        sSoftbanksMap.put(0xe057, R.drawable.emoji_1f603);
        sSoftbanksMap.put(0xe058, R.drawable.emoji_1f61e);
        sSoftbanksMap.put(0xe059, R.drawable.emoji_1f620);
        sSoftbanksMap.put(0xe05a, R.drawable.emoji_1f4a9);
        sSoftbanksMap.put(0xe101, R.drawable.emoji_1f4ea);
        sSoftbanksMap.put(0xe102, R.drawable.emoji_1f4ee);
        sSoftbanksMap.put(0xe103, R.drawable.emoji_1f4e7);
        sSoftbanksMap.put(0xe104, R.drawable.emoji_1f4f2);
        sSoftbanksMap.put(0xe105, R.drawable.emoji_1f61c);
        sSoftbanksMap.put(0xe106, R.drawable.emoji_1f60d);
        sSoftbanksMap.put(0xe107, R.drawable.emoji_1f631);
        sSoftbanksMap.put(0xe108, R.drawable.emoji_1f613);
        sSoftbanksMap.put(0xe109, R.drawable.emoji_1f435);
        sSoftbanksMap.put(0xe10a, R.drawable.emoji_1f419);
        sSoftbanksMap.put(0xe10b, R.drawable.emoji_1f437);
        sSoftbanksMap.put(0xe10c, R.drawable.emoji_1f47d);
        sSoftbanksMap.put(0xe10d, R.drawable.emoji_1f680);
        sSoftbanksMap.put(0xe10e, R.drawable.emoji_1f451);
        sSoftbanksMap.put(0xe10f, R.drawable.emoji_1f4a1);
        sSoftbanksMap.put(0xe110, R.drawable.emoji_1f331);
        sSoftbanksMap.put(0xe111, R.drawable.emoji_1f48f);
        sSoftbanksMap.put(0xe112, R.drawable.emoji_1f381);
        sSoftbanksMap.put(0xe113, R.drawable.emoji_1f52b);
        sSoftbanksMap.put(0xe114, R.drawable.emoji_1f50d);
        sSoftbanksMap.put(0xe115, R.drawable.emoji_1f3c3);
        sSoftbanksMap.put(0xe116, R.drawable.emoji_1f528);
        sSoftbanksMap.put(0xe117, R.drawable.emoji_1f386);
        sSoftbanksMap.put(0xe118, R.drawable.emoji_1f341);
        sSoftbanksMap.put(0xe119, R.drawable.emoji_1f342);
        sSoftbanksMap.put(0xe11a, R.drawable.emoji_1f47f);
        sSoftbanksMap.put(0xe11b, R.drawable.emoji_1f47b);
        sSoftbanksMap.put(0xe11c, R.drawable.emoji_1f480);
        sSoftbanksMap.put(0xe11d, R.drawable.emoji_1f525);
        sSoftbanksMap.put(0xe11e, R.drawable.emoji_1f4bc);
        sSoftbanksMap.put(0xe11f, R.drawable.emoji_1f4ba);
        sSoftbanksMap.put(0xe120, R.drawable.emoji_1f354);
        sSoftbanksMap.put(0xe121, R.drawable.emoji_26f2);
        sSoftbanksMap.put(0xe122, R.drawable.emoji_26fa);
        sSoftbanksMap.put(0xe123, R.drawable.emoji_2668);
        sSoftbanksMap.put(0xe124, R.drawable.emoji_1f3a1);
        sSoftbanksMap.put(0xe125, R.drawable.emoji_1f3ab);
        sSoftbanksMap.put(0xe126, R.drawable.emoji_1f4bf);
        sSoftbanksMap.put(0xe127, R.drawable.emoji_1f4c0);
        sSoftbanksMap.put(0xe128, R.drawable.emoji_1f4fb);
        sSoftbanksMap.put(0xe129, R.drawable.emoji_1f4fc);
        sSoftbanksMap.put(0xe12a, R.drawable.emoji_1f4fa);
        sSoftbanksMap.put(0xe12b, R.drawable.emoji_1f47e);
        sSoftbanksMap.put(0xe12c, R.drawable.emoji_303d);
        sSoftbanksMap.put(0xe12d, R.drawable.emoji_1f004);
        sSoftbanksMap.put(0xe12e, R.drawable.emoji_1f19a);
        sSoftbanksMap.put(0xe12f, R.drawable.emoji_1f4b0);
        sSoftbanksMap.put(0xe130, R.drawable.emoji_1f3af);
        sSoftbanksMap.put(0xe131, R.drawable.emoji_1f3c6);
        sSoftbanksMap.put(0xe132, R.drawable.emoji_1f3c1);
        sSoftbanksMap.put(0xe133, R.drawable.emoji_1f3b0);
        sSoftbanksMap.put(0xe134, R.drawable.emoji_1f40e);
        sSoftbanksMap.put(0xe135, R.drawable.emoji_1f6a4);
        sSoftbanksMap.put(0xe136, R.drawable.emoji_1f6b2);
        sSoftbanksMap.put(0xe137, R.drawable.emoji_1f6a7);
        sSoftbanksMap.put(0xe138, R.drawable.emoji_1f6b9);
        sSoftbanksMap.put(0xe139, R.drawable.emoji_1f6ba);
        sSoftbanksMap.put(0xe13a, R.drawable.emoji_1f6bc);
        sSoftbanksMap.put(0xe13b, R.drawable.emoji_1f489);
        sSoftbanksMap.put(0xe13c, R.drawable.emoji_1f4a4);
        sSoftbanksMap.put(0xe13d, R.drawable.emoji_26a1);
        sSoftbanksMap.put(0xe13e, R.drawable.emoji_1f460);
        sSoftbanksMap.put(0xe13f, R.drawable.emoji_1f6c0);
        sSoftbanksMap.put(0xe140, R.drawable.emoji_1f6bd);
        sSoftbanksMap.put(0xe141, R.drawable.emoji_1f50a);
        sSoftbanksMap.put(0xe142, R.drawable.emoji_1f4e2);
        sSoftbanksMap.put(0xe143, R.drawable.emoji_1f38c);
        sSoftbanksMap.put(0xe144, R.drawable.emoji_1f50f);
        sSoftbanksMap.put(0xe145, R.drawable.emoji_1f513);
        sSoftbanksMap.put(0xe146, R.drawable.emoji_1f306);
        sSoftbanksMap.put(0xe147, R.drawable.emoji_1f373);
        sSoftbanksMap.put(0xe148, R.drawable.emoji_1f4c7);
        sSoftbanksMap.put(0xe149, R.drawable.emoji_1f4b1);
        sSoftbanksMap.put(0xe14a, R.drawable.emoji_1f4b9);
        sSoftbanksMap.put(0xe14b, R.drawable.emoji_1f4e1);
        sSoftbanksMap.put(0xe14c, R.drawable.emoji_1f4aa);
        sSoftbanksMap.put(0xe14d, R.drawable.emoji_1f3e6);
        sSoftbanksMap.put(0xe14e, R.drawable.emoji_1f6a5);
        sSoftbanksMap.put(0xe14f, R.drawable.emoji_1f17f);
        sSoftbanksMap.put(0xe150, R.drawable.emoji_1f68f);
        sSoftbanksMap.put(0xe151, R.drawable.emoji_1f6bb);
        sSoftbanksMap.put(0xe152, R.drawable.emoji_1f46e);
        sSoftbanksMap.put(0xe153, R.drawable.emoji_1f3e3);
        sSoftbanksMap.put(0xe154, R.drawable.emoji_1f3e7);
        sSoftbanksMap.put(0xe155, R.drawable.emoji_1f3e5);
        sSoftbanksMap.put(0xe156, R.drawable.emoji_1f3ea);
        sSoftbanksMap.put(0xe157, R.drawable.emoji_1f3eb);
        sSoftbanksMap.put(0xe158, R.drawable.emoji_1f3e8);
        sSoftbanksMap.put(0xe159, R.drawable.emoji_1f68c);
        sSoftbanksMap.put(0xe15a, R.drawable.emoji_1f695);
        sSoftbanksMap.put(0xe201, R.drawable.emoji_1f6b6);
        sSoftbanksMap.put(0xe202, R.drawable.emoji_1f6a2);
        sSoftbanksMap.put(0xe203, R.drawable.emoji_1f201);
        sSoftbanksMap.put(0xe204, R.drawable.emoji_1f49f);
        sSoftbanksMap.put(0xe205, R.drawable.emoji_2734);
        sSoftbanksMap.put(0xe206, R.drawable.emoji_2733);
        sSoftbanksMap.put(0xe207, R.drawable.emoji_1f51e);
        sSoftbanksMap.put(0xe208, R.drawable.emoji_1f6ad);
        sSoftbanksMap.put(0xe209, R.drawable.emoji_1f530);
        sSoftbanksMap.put(0xe20a, R.drawable.emoji_267f);
        sSoftbanksMap.put(0xe20b, R.drawable.emoji_1f4f6);
        sSoftbanksMap.put(0xe20c, R.drawable.emoji_2665);
        sSoftbanksMap.put(0xe20d, R.drawable.emoji_2666);
        sSoftbanksMap.put(0xe20e, R.drawable.emoji_2660);
        sSoftbanksMap.put(0xe20f, R.drawable.emoji_2663);
        sSoftbanksMap.put(0xe210, R.drawable.emoji_0023);
        sSoftbanksMap.put(0xe211, R.drawable.emoji_27bf);
        sSoftbanksMap.put(0xe212, R.drawable.emoji_1f195);
        sSoftbanksMap.put(0xe213, R.drawable.emoji_1f199);
        sSoftbanksMap.put(0xe214, R.drawable.emoji_1f192);
        sSoftbanksMap.put(0xe215, R.drawable.emoji_1f236);
        sSoftbanksMap.put(0xe216, R.drawable.emoji_1f21a);
        sSoftbanksMap.put(0xe217, R.drawable.emoji_1f237);
        sSoftbanksMap.put(0xe218, R.drawable.emoji_1f238);
        sSoftbanksMap.put(0xe219, R.drawable.emoji_1f534);
        sSoftbanksMap.put(0xe21a, R.drawable.emoji_1f532);
        sSoftbanksMap.put(0xe21b, R.drawable.emoji_1f533);
        sSoftbanksMap.put(0xe21c, R.drawable.emoji_0031);
        sSoftbanksMap.put(0xe21d, R.drawable.emoji_0032);
        sSoftbanksMap.put(0xe21e, R.drawable.emoji_0033);
        sSoftbanksMap.put(0xe21f, R.drawable.emoji_0034);
        sSoftbanksMap.put(0xe220, R.drawable.emoji_0035);
        sSoftbanksMap.put(0xe221, R.drawable.emoji_0036);
        sSoftbanksMap.put(0xe222, R.drawable.emoji_0037);
        sSoftbanksMap.put(0xe223, R.drawable.emoji_0038);
        sSoftbanksMap.put(0xe224, R.drawable.emoji_0039);
        sSoftbanksMap.put(0xe225, R.drawable.emoji_0030);
        sSoftbanksMap.put(0xe226, R.drawable.emoji_1f250);
        sSoftbanksMap.put(0xe227, R.drawable.emoji_1f239);
        sSoftbanksMap.put(0xe228, R.drawable.emoji_1f202);
        sSoftbanksMap.put(0xe229, R.drawable.emoji_1f194);
        sSoftbanksMap.put(0xe22a, R.drawable.emoji_1f235);
        sSoftbanksMap.put(0xe22b, R.drawable.emoji_1f233);
        sSoftbanksMap.put(0xe22c, R.drawable.emoji_1f22f);
        sSoftbanksMap.put(0xe22d, R.drawable.emoji_1f23a);
        sSoftbanksMap.put(0xe22e, R.drawable.emoji_1f446);
        sSoftbanksMap.put(0xe22f, R.drawable.emoji_1f447);
        sSoftbanksMap.put(0xe230, R.drawable.emoji_1f448);
        sSoftbanksMap.put(0xe231, R.drawable.emoji_1f449);
        sSoftbanksMap.put(0xe232, R.drawable.emoji_2b06);
        sSoftbanksMap.put(0xe233, R.drawable.emoji_2b07);
        sSoftbanksMap.put(0xe234, R.drawable.emoji_27a1);
        sSoftbanksMap.put(0xe235, R.drawable.emoji_1f519);
        sSoftbanksMap.put(0xe236, R.drawable.emoji_2197);
        sSoftbanksMap.put(0xe237, R.drawable.emoji_2196);
        sSoftbanksMap.put(0xe238, R.drawable.emoji_2198);
        sSoftbanksMap.put(0xe239, R.drawable.emoji_2199);
        sSoftbanksMap.put(0xe23a, R.drawable.emoji_25b6);
        sSoftbanksMap.put(0xe23b, R.drawable.emoji_25c0);
        sSoftbanksMap.put(0xe23c, R.drawable.emoji_23e9);
        sSoftbanksMap.put(0xe23d, R.drawable.emoji_23ea);
        sSoftbanksMap.put(0xe23e, R.drawable.emoji_1f52e);
        sSoftbanksMap.put(0xe23f, R.drawable.emoji_2648);
        sSoftbanksMap.put(0xe240, R.drawable.emoji_2649);
        sSoftbanksMap.put(0xe241, R.drawable.emoji_264a);
        sSoftbanksMap.put(0xe242, R.drawable.emoji_264b);
        sSoftbanksMap.put(0xe243, R.drawable.emoji_264c);
        sSoftbanksMap.put(0xe244, R.drawable.emoji_264d);
        sSoftbanksMap.put(0xe245, R.drawable.emoji_264e);
        sSoftbanksMap.put(0xe246, R.drawable.emoji_264f);
        sSoftbanksMap.put(0xe247, R.drawable.emoji_2650);
        sSoftbanksMap.put(0xe248, R.drawable.emoji_2651);
        sSoftbanksMap.put(0xe249, R.drawable.emoji_2652);
        sSoftbanksMap.put(0xe24a, R.drawable.emoji_2653);
        sSoftbanksMap.put(0xe24b, R.drawable.emoji_26ce);
        sSoftbanksMap.put(0xe24c, R.drawable.emoji_1f51d);
        sSoftbanksMap.put(0xe24d, R.drawable.emoji_1f197);
        sSoftbanksMap.put(0xe24e, R.drawable.emoji_00a9);
        sSoftbanksMap.put(0xe24f, R.drawable.emoji_00ae);
        sSoftbanksMap.put(0xe250, R.drawable.emoji_1f4f3);
        sSoftbanksMap.put(0xe251, R.drawable.emoji_1f4f4);
        sSoftbanksMap.put(0xe252, R.drawable.emoji_26a0);
        sSoftbanksMap.put(0xe253, R.drawable.emoji_1f481);
        sSoftbanksMap.put(0xe301, R.drawable.emoji_1f4c3);
        sSoftbanksMap.put(0xe302, R.drawable.emoji_1f454);
        sSoftbanksMap.put(0xe303, R.drawable.emoji_1f33a);
        sSoftbanksMap.put(0xe304, R.drawable.emoji_1f337);
        sSoftbanksMap.put(0xe305, R.drawable.emoji_1f33b);
        sSoftbanksMap.put(0xe306, R.drawable.emoji_1f490);
        sSoftbanksMap.put(0xe307, R.drawable.emoji_1f334);
        sSoftbanksMap.put(0xe308, R.drawable.emoji_1f335);
        sSoftbanksMap.put(0xe309, R.drawable.emoji_1f6be);
        sSoftbanksMap.put(0xe30a, R.drawable.emoji_1f3a7);
        sSoftbanksMap.put(0xe30b, R.drawable.emoji_1f376);
        sSoftbanksMap.put(0xe30c, R.drawable.emoji_1f37b);
        sSoftbanksMap.put(0xe30d, R.drawable.emoji_3297);
        sSoftbanksMap.put(0xe30e, R.drawable.emoji_1f6ac);
        sSoftbanksMap.put(0xe30f, R.drawable.emoji_1f48a);
        sSoftbanksMap.put(0xe310, R.drawable.emoji_1f388);
        sSoftbanksMap.put(0xe311, R.drawable.emoji_1f4a3);
        sSoftbanksMap.put(0xe312, R.drawable.emoji_1f389);
        sSoftbanksMap.put(0xe313, R.drawable.emoji_2702);
        sSoftbanksMap.put(0xe314, R.drawable.emoji_1f380);
        sSoftbanksMap.put(0xe315, R.drawable.emoji_3299);
        sSoftbanksMap.put(0xe316, R.drawable.emoji_1f4bd);
        sSoftbanksMap.put(0xe317, R.drawable.emoji_1f4e3);
        sSoftbanksMap.put(0xe318, R.drawable.emoji_1f452);
        sSoftbanksMap.put(0xe319, R.drawable.emoji_1f457);
        sSoftbanksMap.put(0xe31a, R.drawable.emoji_1f461);
        sSoftbanksMap.put(0xe31b, R.drawable.emoji_1f462);
        sSoftbanksMap.put(0xe31c, R.drawable.emoji_1f484);
        sSoftbanksMap.put(0xe31d, R.drawable.emoji_1f485);
        sSoftbanksMap.put(0xe31e, R.drawable.emoji_1f486);
        sSoftbanksMap.put(0xe31f, R.drawable.emoji_1f487);
        sSoftbanksMap.put(0xe320, R.drawable.emoji_1f488);
        sSoftbanksMap.put(0xe321, R.drawable.emoji_1f458);
        sSoftbanksMap.put(0xe322, R.drawable.emoji_1f459);
        sSoftbanksMap.put(0xe323, R.drawable.emoji_1f45c);
        sSoftbanksMap.put(0xe324, R.drawable.emoji_1f3ac);
        sSoftbanksMap.put(0xe325, R.drawable.emoji_1f514);
        sSoftbanksMap.put(0xe326, R.drawable.emoji_1f3b6);
        sSoftbanksMap.put(0xe327, R.drawable.emoji_1f493);
        sSoftbanksMap.put(0xe328, R.drawable.emoji_1f48c);
        sSoftbanksMap.put(0xe329, R.drawable.emoji_1f498);
        sSoftbanksMap.put(0xe32a, R.drawable.emoji_1f499);
        sSoftbanksMap.put(0xe32b, R.drawable.emoji_1f49a);
        sSoftbanksMap.put(0xe32c, R.drawable.emoji_1f49b);
        sSoftbanksMap.put(0xe32d, R.drawable.emoji_1f49c);
        sSoftbanksMap.put(0xe32e, R.drawable.emoji_2728);
        sSoftbanksMap.put(0xe32f, R.drawable.emoji_2b50);
        sSoftbanksMap.put(0xe330, R.drawable.emoji_1f4a8);
        sSoftbanksMap.put(0xe331, R.drawable.emoji_1f4a6);
        sSoftbanksMap.put(0xe332, R.drawable.emoji_2b55);
        sSoftbanksMap.put(0xe333, R.drawable.emoji_2716);
        sSoftbanksMap.put(0xe334, R.drawable.emoji_1f4a2);
        sSoftbanksMap.put(0xe335, R.drawable.emoji_1f31f);
        sSoftbanksMap.put(0xe336, R.drawable.emoji_2754);
        sSoftbanksMap.put(0xe337, R.drawable.emoji_2755);
        sSoftbanksMap.put(0xe338, R.drawable.emoji_1f375);
        sSoftbanksMap.put(0xe339, R.drawable.emoji_1f35e);
        sSoftbanksMap.put(0xe33a, R.drawable.emoji_1f366);
        sSoftbanksMap.put(0xe33b, R.drawable.emoji_1f35f);
        sSoftbanksMap.put(0xe33c, R.drawable.emoji_1f361);
        sSoftbanksMap.put(0xe33d, R.drawable.emoji_1f358);
        sSoftbanksMap.put(0xe33e, R.drawable.emoji_1f35a);
        sSoftbanksMap.put(0xe33f, R.drawable.emoji_1f35d);
        sSoftbanksMap.put(0xe340, R.drawable.emoji_1f35c);
        sSoftbanksMap.put(0xe341, R.drawable.emoji_1f35b);
        sSoftbanksMap.put(0xe342, R.drawable.emoji_1f359);
        sSoftbanksMap.put(0xe343, R.drawable.emoji_1f362);
        sSoftbanksMap.put(0xe344, R.drawable.emoji_1f363);
        sSoftbanksMap.put(0xe345, R.drawable.emoji_1f34e);
        sSoftbanksMap.put(0xe346, R.drawable.emoji_1f34a);
        sSoftbanksMap.put(0xe347, R.drawable.emoji_1f353);
        sSoftbanksMap.put(0xe348, R.drawable.emoji_1f349);
        sSoftbanksMap.put(0xe349, R.drawable.emoji_1f345);
        sSoftbanksMap.put(0xe34a, R.drawable.emoji_1f346);
        sSoftbanksMap.put(0xe34b, R.drawable.emoji_1f382);
        sSoftbanksMap.put(0xe34c, R.drawable.emoji_1f371);
        sSoftbanksMap.put(0xe34d, R.drawable.emoji_1f372);
        sSoftbanksMap.put(0xe401, R.drawable.emoji_1f625);
        sSoftbanksMap.put(0xe402, R.drawable.emoji_1f60f);
        sSoftbanksMap.put(0xe403, R.drawable.emoji_1f614);
        sSoftbanksMap.put(0xe404, R.drawable.emoji_1f601);
        sSoftbanksMap.put(0xe405, R.drawable.emoji_1f609);
        sSoftbanksMap.put(0xe406, R.drawable.emoji_1f623);
        sSoftbanksMap.put(0xe407, R.drawable.emoji_1f616);
        sSoftbanksMap.put(0xe408, R.drawable.emoji_1f62a);
        sSoftbanksMap.put(0xe409, R.drawable.emoji_1f445);
        sSoftbanksMap.put(0xe40a, R.drawable.emoji_1f606);
        sSoftbanksMap.put(0xe40b, R.drawable.emoji_1f628);
        sSoftbanksMap.put(0xe40c, R.drawable.emoji_1f637);
        sSoftbanksMap.put(0xe40d, R.drawable.emoji_1f633);
        sSoftbanksMap.put(0xe40e, R.drawable.emoji_1f612);
        sSoftbanksMap.put(0xe40f, R.drawable.emoji_1f630);
        sSoftbanksMap.put(0xe410, R.drawable.emoji_1f632);
        sSoftbanksMap.put(0xe411, R.drawable.emoji_1f62d);
        sSoftbanksMap.put(0xe412, R.drawable.emoji_1f602);
        sSoftbanksMap.put(0xe413, R.drawable.emoji_1f622);
        sSoftbanksMap.put(0xe414, R.drawable.emoji_263a);
        sSoftbanksMap.put(0xe415, R.drawable.emoji_1f605);
        sSoftbanksMap.put(0xe416, R.drawable.emoji_1f621);
        sSoftbanksMap.put(0xe417, R.drawable.emoji_1f61a);
        sSoftbanksMap.put(0xe418, R.drawable.emoji_1f618);
        sSoftbanksMap.put(0xe419, R.drawable.emoji_1f440);
        sSoftbanksMap.put(0xe41a, R.drawable.emoji_1f443);
        sSoftbanksMap.put(0xe41b, R.drawable.emoji_1f442);
        sSoftbanksMap.put(0xe41c, R.drawable.emoji_1f444);
        sSoftbanksMap.put(0xe41d, R.drawable.emoji_1f64f);
        sSoftbanksMap.put(0xe41e, R.drawable.emoji_1f44b);
        sSoftbanksMap.put(0xe41f, R.drawable.emoji_1f44f);
        sSoftbanksMap.put(0xe420, R.drawable.emoji_1f44c);
        sSoftbanksMap.put(0xe421, R.drawable.emoji_1f44e);
        sSoftbanksMap.put(0xe422, R.drawable.emoji_1f450);
        sSoftbanksMap.put(0xe423, R.drawable.emoji_1f645);
        sSoftbanksMap.put(0xe424, R.drawable.emoji_1f646);
        sSoftbanksMap.put(0xe425, R.drawable.emoji_1f491);
        sSoftbanksMap.put(0xe426, R.drawable.emoji_1f647);
        sSoftbanksMap.put(0xe427, R.drawable.emoji_1f64c);
        sSoftbanksMap.put(0xe428, R.drawable.emoji_1f46b);
        sSoftbanksMap.put(0xe429, R.drawable.emoji_1f46f);
        sSoftbanksMap.put(0xe42a, R.drawable.emoji_1f3c0);
        sSoftbanksMap.put(0xe42b, R.drawable.emoji_1f3c8);
        sSoftbanksMap.put(0xe42c, R.drawable.emoji_1f3b1);
        sSoftbanksMap.put(0xe42d, R.drawable.emoji_1f3ca);
        sSoftbanksMap.put(0xe42e, R.drawable.emoji_1f699);
        sSoftbanksMap.put(0xe42f, R.drawable.emoji_1f69a);
        sSoftbanksMap.put(0xe430, R.drawable.emoji_1f692);
        sSoftbanksMap.put(0xe431, R.drawable.emoji_1f691);
        sSoftbanksMap.put(0xe432, R.drawable.emoji_1f693);
        sSoftbanksMap.put(0xe433, R.drawable.emoji_1f3a2);
        sSoftbanksMap.put(0xe434, R.drawable.emoji_1f687);
        sSoftbanksMap.put(0xe435, R.drawable.emoji_1f684);
        sSoftbanksMap.put(0xe436, R.drawable.emoji_1f38d);
        sSoftbanksMap.put(0xe437, R.drawable.emoji_1f49d);
        sSoftbanksMap.put(0xe438, R.drawable.emoji_1f38e);
        sSoftbanksMap.put(0xe439, R.drawable.emoji_1f393);
        sSoftbanksMap.put(0xe43a, R.drawable.emoji_1f392);
        sSoftbanksMap.put(0xe43b, R.drawable.emoji_1f38f);
        sSoftbanksMap.put(0xe43c, R.drawable.emoji_1f302);
        sSoftbanksMap.put(0xe43d, R.drawable.emoji_1f492);
        sSoftbanksMap.put(0xe43e, R.drawable.emoji_1f30a);
        sSoftbanksMap.put(0xe43f, R.drawable.emoji_1f367);
        sSoftbanksMap.put(0xe440, R.drawable.emoji_1f387);
        sSoftbanksMap.put(0xe441, R.drawable.emoji_1f41a);
        sSoftbanksMap.put(0xe442, R.drawable.emoji_1f390);
        sSoftbanksMap.put(0xe443, R.drawable.emoji_1f300);
        sSoftbanksMap.put(0xe444, R.drawable.emoji_1f33e);
        sSoftbanksMap.put(0xe445, R.drawable.emoji_1f383);
        sSoftbanksMap.put(0xe446, R.drawable.emoji_1f391);
        sSoftbanksMap.put(0xe447, R.drawable.emoji_1f343);
        sSoftbanksMap.put(0xe448, R.drawable.emoji_1f385);
        sSoftbanksMap.put(0xe449, R.drawable.emoji_1f305);
        sSoftbanksMap.put(0xe44a, R.drawable.emoji_1f307);
        sSoftbanksMap.put(0xe44b, R.drawable.emoji_1f303);
        sSoftbanksMap.put(0xe44b, R.drawable.emoji_1f30c);
        sSoftbanksMap.put(0xe44c, R.drawable.emoji_1f308);
        sSoftbanksMap.put(0xe501, R.drawable.emoji_1f3e9);
        sSoftbanksMap.put(0xe502, R.drawable.emoji_1f3a8);
        sSoftbanksMap.put(0xe503, R.drawable.emoji_1f3a9);
        sSoftbanksMap.put(0xe504, R.drawable.emoji_1f3ec);
        sSoftbanksMap.put(0xe505, R.drawable.emoji_1f3ef);
        sSoftbanksMap.put(0xe506, R.drawable.emoji_1f3f0);
        sSoftbanksMap.put(0xe507, R.drawable.emoji_1f3a6);
        sSoftbanksMap.put(0xe508, R.drawable.emoji_1f3ed);
        sSoftbanksMap.put(0xe509, R.drawable.emoji_1f5fc);
        sSoftbanksMap.put(0xe50b, R.drawable.emoji_1f1ef_1f1f5);
        sSoftbanksMap.put(0xe50c, R.drawable.emoji_1f1fa_1f1f8);
        sSoftbanksMap.put(0xe50d, R.drawable.emoji_1f1eb_1f1f7);
        sSoftbanksMap.put(0xe50e, R.drawable.emoji_1f1e9_1f1ea);
        sSoftbanksMap.put(0xe50f, R.drawable.emoji_1f1ee_1f1f9);
        sSoftbanksMap.put(0xe510, R.drawable.emoji_1f1ec_1f1e7);
        sSoftbanksMap.put(0xe511, R.drawable.emoji_1f1ea_1f1f8);
        sSoftbanksMap.put(0xe512, R.drawable.emoji_1f1f7_1f1fa);
        sSoftbanksMap.put(0xe513, R.drawable.emoji_1f1e8_1f1f3);
        sSoftbanksMap.put(0xe514, R.drawable.emoji_1f1f0_1f1f7);
        sSoftbanksMap.put(0xe515, R.drawable.emoji_1f471);
        sSoftbanksMap.put(0xe516, R.drawable.emoji_1f472);
        sSoftbanksMap.put(0xe517, R.drawable.emoji_1f473);
        sSoftbanksMap.put(0xe518, R.drawable.emoji_1f474);
        sSoftbanksMap.put(0xe519, R.drawable.emoji_1f475);
        sSoftbanksMap.put(0xe51a, R.drawable.emoji_1f476);
        sSoftbanksMap.put(0xe51b, R.drawable.emoji_1f477);
        sSoftbanksMap.put(0xe51c, R.drawable.emoji_1f478);
        sSoftbanksMap.put(0xe51d, R.drawable.emoji_1f5fd);
        sSoftbanksMap.put(0xe51e, R.drawable.emoji_1f482);
        sSoftbanksMap.put(0xe51f, R.drawable.emoji_1f483);
        sSoftbanksMap.put(0xe520, R.drawable.emoji_1f42c);
        sSoftbanksMap.put(0xe521, R.drawable.emoji_1f426);
        sSoftbanksMap.put(0xe522, R.drawable.emoji_1f420);
        sSoftbanksMap.put(0xe523, R.drawable.emoji_1f423);
        sSoftbanksMap.put(0xe524, R.drawable.emoji_1f439);
        sSoftbanksMap.put(0xe525, R.drawable.emoji_1f41b);
        sSoftbanksMap.put(0xe526, R.drawable.emoji_1f418);
        sSoftbanksMap.put(0xe527, R.drawable.emoji_1f428);
        sSoftbanksMap.put(0xe528, R.drawable.emoji_1f412);
        sSoftbanksMap.put(0xe529, R.drawable.emoji_1f411);
        sSoftbanksMap.put(0xe52a, R.drawable.emoji_1f43a);
        sSoftbanksMap.put(0xe52b, R.drawable.emoji_1f42e);
        sSoftbanksMap.put(0xe52c, R.drawable.emoji_1f430);
        sSoftbanksMap.put(0xe52d, R.drawable.emoji_1f40d);
        sSoftbanksMap.put(0xe52e, R.drawable.emoji_1f414);
        sSoftbanksMap.put(0xe52f, R.drawable.emoji_1f417);
        sSoftbanksMap.put(0xe530, R.drawable.emoji_1f42b);
        sSoftbanksMap.put(0xe531, R.drawable.emoji_1f438);
        sSoftbanksMap.put(0xe532, R.drawable.emoji_1f170);
        sSoftbanksMap.put(0xe533, R.drawable.emoji_1f171);
        sSoftbanksMap.put(0xe534, R.drawable.emoji_1f18e);
        sSoftbanksMap.put(0xe535, R.drawable.emoji_1f17e);
        sSoftbanksMap.put(0xe536, R.drawable.emoji_1f43e);
        sSoftbanksMap.put(0xe537, R.drawable.emoji_2122);*/
    }

    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    private static int getEmojiResource(Context context, int codePoint) {
        return sEmojisMap.get(codePoint);
    }

    private static int getSoftbankEmojiResource(char c) {
        return sSoftbanksMap.get(c);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize) {
        addEmojis(context, text, emojiSize, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param index
     * @param length
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length) {
        addEmojis(context, text, emojiSize, index, length, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, 0, -1, useSystemDefault);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault) {
            return;
        }
        setEmojiIcon(context, text, emojiSize);
        setColorUserNameMentioned(context, text);
    }
    private static Pattern maskEmojiIcon = Pattern.compile(":{1}+(\\w|_|\\d|\\+|\\-)+:{1}+");
    private static void setEmojiIcon(Context context, Spannable text, int emojiSize) {
        String fulltext = text.toString();
        // Matcher matching the pattern
        Matcher matcher = maskEmojiIcon.matcher(fulltext);
        int idx = 0;
        while (matcher.find()) {
            int k = matcher.start();
            int m = matcher.end();
            String subtext = fulltext.substring(k,m);
            if(msEmojisMap.containsKey(subtext)) {
                int icon = msEmojisMap.get(subtext);
                text.setSpan(new EmojiconSpan(context, icon, emojiSize), k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static Pattern maskNameUser = Pattern.compile("@[a-zA-Z0-9]+");
    public static String myName = "";

    private static void setColorUserNameMentioned(Context context, Spannable text) {
        String fulltext = text.toString();
        // Matcher matching the pattern
        Matcher matcher = maskNameUser.matcher(fulltext);
        int idx = 0;
        while (matcher.find()) {
            int k = matcher.start();
            int m = matcher.end();

            String subtext = fulltext.substring(k, m);
            if(subtext.equalsIgnoreCase(myName) || subtext.equalsIgnoreCase("@all") )
                text.setSpan(new BackgroundSpan(10,0xff337CCF, 0xffffffff, 0xff4688D3), k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else
                text.setSpan(new BackgroundSpan(10,0xffD6D6D6, 0xff000000, 0xffcccccc), k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //text.setSpan(new BackgroundColorSpan(color), k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static class BackgroundSpan extends ReplacementSpan implements LineBackgroundSpan {
        int layoutLeft = 0;
        int layoutRight = 0;
        private int mWidth;
        float round;
        int backgroundColor, textColor, strokeColor;

        public BackgroundSpan(float round, int backgroundColor, int textColor, int strokeColor) {
            this.round = round;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            this.strokeColor = strokeColor;
        }

        @Override
        public void drawBackground(Canvas c, Paint p,
                                   int left, int right,
                                   int top, int baseline, int bottom,
                                   CharSequence text, int start, int end,
                                   int lnum) {
            Rect clipRect = new Rect();
            c.getClipBounds(clipRect);
            layoutLeft = clipRect.left;
            layoutRight = clipRect.right;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            mWidth = (int) paint.measureText(text, start, end) + 2;
            return mWidth;

        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            float textWidth = paint.measureText(text, start, end);
            paint.setColor(backgroundColor);
            RectF rect = new RectF(x, top, x + mWidth, bottom);
            canvas.drawRoundRect(rect, round, round, paint);
            paint.setColor(textColor);
            canvas.drawText(text, start, end, x + 1, y - 1, paint);

            paint.setColor(strokeColor); // you can change color of your border here, to other color
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            canvas.drawRoundRect(rect, round, round, paint);
        }


    }
}
