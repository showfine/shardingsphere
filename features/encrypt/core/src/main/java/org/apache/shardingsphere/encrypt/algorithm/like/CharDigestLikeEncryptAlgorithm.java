/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.algorithm.like;

import com.google.common.base.Strings;
import lombok.Getter;
import org.apache.shardingsphere.encrypt.exception.algorithm.EncryptAlgorithmInitializationException;
import org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm;
import org.apache.shardingsphere.encrypt.spi.context.EncryptContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Char digest like encrypt algorithm.
 */
public final class CharDigestLikeEncryptAlgorithm implements EncryptAlgorithm<Object, String> {
    
    private static final String DELTA = "delta";
    
    private static final String MASK = "mask";
    
    private static final String START = "start";
    
    private static final String DICT = "dict";
    
    private static final int DEFAULT_DELTA = 1;
    
    private static final int DEFAULT_MASK = 0b1111_1011_1101;
    
    private static final int DEFAULT_START = 0x4e00;
    
    private static final String DEFAULT_DICT = "谤杉巫夏辅俯鸵直菱梨滨头矾讯芯巡泥簇何逊谜颐男拴冰响贰哈雄赌密愚思戊叔苔肩"
            + "亏硕递意忻雷扫旋谭坎散拷廖余饮囤咖小娄藻唇妙枣豺料淡器谈赘托察湿莽算诽嘛越魁涤缮漱镣豁孔萝脾稻杠遮漏浪常滦呸层涪粪乐萄褐榜"
            + "程伙椭句蚤入概鹰惨闻舶封瘴创详筹边络乞洪咯丈眯弛娱狸锐俄痔向片壬二剧壳羌劳澳到箔慧醛俏硬读宁谨谋捶牧豆脱饰肛渠坞富揉赤帽能"
            + "巩雁题姬崔旁袄主蝇屏撼粗贡刃办肉穷态柳嚼鸥哨兔瓦籍谊唯指混丰肤值邹脸爪摄兑券浆薄漾盟磊牺筑锤匿碑萍拌醉焊扞韭群擦飞尺从咆蜘"
            + "辗帅蘑懦彭翠痛联热惮溯啦乏沏颈渔奋曲衣焦迄喳狞登冯制腐青围业瑶拨碉赢幽开狠耐熄培朝编堂嫌媚王跳剖骸四爬氛既遣鱼氢利龟爱斯毅"
            + "屠氦止邀厉员拭佛针牲菌耳罕诺虐倦瓣渐丛哼麻饱且楷诀港贤酣李管撅茧犊关聪凉试弦煤谐掣戈膊藩翘扬契货鸿逮众末汾芍凋撬罩数反罢背"
            + "睛琵觉芒箩嫂钡豹峭箱纤拄哑留损售布旅搁蒲藕阶郝披恃邢落殉证盗鞠竟亢勺刀足畦熙帛曹娇宗亭蔑靖须医疮凯灾鸡蔫室耪沃阂伎墨卷随釜"
            + "巾浴领臣啊绊纫较后圃桨厚教磋颖姥你荤丧沪芳栋休未茁舵林啥国糖规括搪扯补摹近绸履瞄兄般轧坛够雀烂锨嘱聚陵篷调躬印祈挟胶饯雪甸"
            + "渤当裕汝鲸秽咬岸解剂丁勉鞋缺雨觅伶顿镁掠鸦埔郡场欧商推硒院挎剃齐挡辟棠酚钨隙季沫熊夺孩釉厌复蕴连玫挫雅侦宿衍撰综柱罗硅搏昧"
            + "丢块盂榨泌詹扶誓押湘纠捕但缠乘滇祷请库域液简呜杀咨爵参死确期怪森蛊喘听幅撞哗绘吨盎性锯吼蛤栈镜靡潭裴汽韧么篆缴儡霄敦健订右"
            + "蜕绣符黎匝撒店住羊晤不滔慑臃咙墙丫毖蹿碱灿陶愧颜巴剑璃缀担蕾绩谬记劲疲紊妮谅叮毒携膀桥河惯空迎练厅胃峪窍搜营扔完避劣矢放腺"
            + "琶牌些茬仰碳滓暖威扮洋镭辆渝科抡独稗香宽刷粮眩此掖拟晕葱抱振弱骋磨阐投羽痒玻韩先涝辑肘滤郑厨僧把歉皿刹刮眼援作竞祥纵术犹扼"
            + "稚征肄牟渣码及返应励洲镐朱产老功亚炮借红鉴安暇底救泄拙拓横赎溢姑叉卫篙智丽泽岂待虏伞摈袁苗更氖粤兼粕鹅褪表辉缔统滑纹憨许涧"
            + "迅她铡为喇抄世淑良宵皂折翅碍庶窗沦月盔棒厂臻气猩呻乖唁懊屈微谗踊甩仕乓磅抨倒墟助仓盈辛屑苑怯弥糠窜勇固塔胸膛第潍烹彦牵捧宫"
            + "仇摩掌耍苇舆个麦停猖贞衰赠橇琴脊赦皱假穴瞅寻姨肖比池涂抗猴草矫孤都炳趴曼蛛昆漂姐氏舒秆呼骚纶雇椎蜂梢每腕顽框搽冉奉菏张里钻"
            + "精血燥冻排曝啤孝尹侍瞥濒区踏恕戴蓟馈惜敲堰洗诱灌潮嘎窃葛只警泅狮吧溃糙鳖的烟汪皮哦细策舞铱兽叁阁财喻恶蒜零酶遇沛筐獭闯枫祭"
            + "吉弊棺蚁话俺怜扑烈梗建蛰瀑汹惹检敷颅蝉逸倍胡酪炼晦铝汀秉拽疚仿允有还滁萨滴癣击抵辽女稳鄙果炸埋译棱候沂辐翻舔信荐柞泞沙袱夷"
            + "绦样卑涉垛婆粟喷论促貌窝寇巧饲啡殿春沼塞贺卢幕姿骡哩樟吹叭短蚊挛条哪鲜蛔婚帘樱黑旗致驾髓茫咋聊椿运戚堪执宏庆箕苦彰防岗评使"
            + "事吃凑踪渺烛涯糊某缆云锭楞夯浦跃绿淫烙吓撵迟炔戎勤煎腿赴渡隔窑遭咕酿贱暮湖俱嗅俩逾胯雌梭罐贩迹夸然词晶线桐蹈锗喂旺幢庐怠曙"
            + "腾剪充融测郴曾牛因残炭甚忿族物房文纸寓绎饵导粥育掘麓风唐积浊巨株面稠吏眉雍杆贿伴锈判蒋澜壹昔八木孟户肯旬禁腊裙欣彤蓑甘床京"
            + "在具枕讳偏猿汉宝佃枝船台哟呆早错讽批掸驶盲斌所新丘佰肢索倾兢替誊阉斡钉街剥飘息丑钥菩感趋份冲怕舰猛狱崇控史虽侄潦约愉呛据镑"
            + "福忽眨紫敏状伟秀砂居肌侥者荔等私型争拎毙柿神函狂迪耀嗜睬寺载触毡板茂虱淬叙通蛀裁涟真舅啪适军级攻淄犀酞俭辞博痢卵荷情饶坡俗"
            + "俐韶冕了鄂坯戮丸议尊刨球噪呕闽舀赖豫狐疗伊垒孵筒流湍释祸饿斜馏讣挣抓酒盏介钙迁娠阑蘸抚猎又寄驯铲汕址饼日让篮操三篡儿备忧十"
            + "杂湾悍间变骨武鲁谷蝶捌焚灶妨唉肆槛锦烫镰射往鸭异遍饺抑帧激铂碘贷瞻深噶构旱孽餐退骇肺妖疆鞭起乱堑削米亲涵叛绽达逝砚上汗抛久"
            + "诊陀侧肥毯查法乌供洛勋理仆仍透蔚沟盘益佳酗挤胚亡赃呀纱谁拉蒸碎酋怀卤清恩矿失捎矗朵脖侗羔炽速炎命爸耻识谱选其蛮膳霖似蚜御朴"
            + "疤婪瓤循悯萤骑蹋捐惭猫抹酱慈择奖低郸逃盖伏护绅叶滞敌亩伸檄名兹琅牡号坦斥坷阵守努遏睁痰吝圭娟赶喜歼踌吗德芽驰怨漳启景谦炊徐"
            + "瘫疼宰栖吻冬枉水遗讲萎穆力战却沥圣汞召偷蔽捏立荧莆仲棕嗓埠坍别核蓉隐侨婿挨或齿织冶再佐污转悉龙黍辕榆吸锰椅畸咱泡伦插隧葬列"
            + "诞逛颗快徘县赊浚箭瑞胖稼奏瑚衔畔金侵痹苞吴蓬闭巢笋谴况寡瓷盅钵卒肮浸由惕惠惺路掀寨巷套朽沽膏骗贯政录掩部鸟楼界凶峻娶喀菜翼"
            + "毗泊涸梅帕校衫禹囚夫冤川获尔付岛腹殴霜帚沈桶甥睫愿漫马诛弄描菲弯组饭村迢用茵靛戏妻委园枯抒帖倡冠歹赫迭拿摔霉半梧佑邮没奎妈"
            + "茨咒襟捂臆好珠玩易多苏欺光椽堵恬强决柑归扇咐典痴椰尾己删澡胳挞芭鹤奇樊洞贪映脏役彝擒藉碾鞍馆狗瑰湛拧哇勒否腔引舱幼赛杭垄蕊"
            + "北会粘芝队胀垢承预瓜君梆郎绍架钟惧乍埃耕蹦奴续童次萧卧乎乒漆擅筋晌缝裸正寐翌僻殃晓慢午笔普局便捆锻枢打根酬栓环炒竭佬雹垂影"
            + "抢遵本番销离俘陪注朗骤悟述嫉虑焉屋萌吟炬天亮疯兆贮狡翔口式基杯赣束舌槽斩皋蜒已拆之迫龚钩纪寅原坐贫骂裔柒钧互嘻送哮岭浩莲淖"
            + "墓惩灭找傻栽顷狭蹭譬敢硼大怖烽例夹莫徊椒万窖暂芹耽超烃抽攒谢爷妄荫褥脐洱泳稍诈坊来牢东劝净宛尘邻率疹卸砖弘尚呐凳历辣极腮潞"
            + "缚您瓮嘿苍朋攀姆蔼邦鸳翟游身造浓墩祝铣监憋赋橙蓝冷衅搬铃对唾郭茶褂士浅免邪侈揣逼粉翁灰白袋坝泛戳躲同干问绳栅钱烷菇磷绑垃坏"
            + "钒伐验荚儒偶技棵梳镍瞳橱按纂演淘痕笛挚傍傀忌谚腋悠拢撩叠顾淹蛇陆拼土兴郧菊矮酝醒滋彬滥壕睡终迸恭乾少芜茹娩痈爽输寿塘姓皑限"
            + "着羡躺腥搔砌寒怎琐降躇绕疏夜鸯闲谓禄圈秘软诣糯雾哎网斟胁坤揭吩灼陌晒农挽必民总圾臼庄锌洁念石位隶蓖囱董盼脓临求糕悦党胰予呵"
            + "汲长旧烯揪刘受桩江砍捞今课勘团方讫祖差穿波滚下峨凹掺铺瞧匪囊薯珊虹鹊惋优亿佯说鳃焰析慷嚣捉醇桅劫泵目昭逞屉娘醋特崖招稿买纯"
            + "犬心榔凤绥施塌芦装奄桔颧缅松角自舷笼拳传械刺经蚕语甭百脂炙支绒闰伤晋憎疵迷狼畏钮望摊以聋得轮淳胜琢交溶喝刁杖知增双驳桂拾溪"
            + "阎苛蜗瘤泼嚎瑟带梦蜜鬃龄忘坠擞瘦垣径煽嫡声秤暴西刻谰才锣殖活嵌车暗盐静签臂啮禾倘惊揽恨孰磁桑外镀绢刊尽锚蔓鼻首笑拜惟袭也频"
            + "烤系泻扁恐趟塑碌秒时庸阅鳞艾询置卓困溉尧颓去卯戌岿模紧海绪檀改嗽烁厩唆咳配膝彪巍伺署栏铸走匣淌加笨睦昼视擎肿访烦卞拔柜济酵"
            + "诉五行帆亨陕搓我炉晚筏礼出陨胆仅寝谩灸悼锹歌仁移虎抖树嘲勾桓研略侩断绝渭啼赞看疽熔耶尿端蛾写尉切秦懂结现除副艺嘴戍甄铆障机"
            + "嗣骏岳妆诗焙棚闸叫楔蚂蛆邯瘁韵逻票茅陡帜崎攘柠律憾犁琳撇馁则献广凡回蚀斧重粱嗡踢驭涛葫父盯楚闹窄勿给疫共癌魄故修曳姚站素幸"
            + "寂攫吵钾劈阮势挠内专类严友馅凭蒙垦败工肪申祁字庚槐摸沤繁剩享敖哆震棉始宇段葵额搭赐刽旦油乡娥氧拘该撮高继哄辙洽佣罚剿仟生渗"
            + "满拐庭歇另杰疟就晰妹窒裂肃撑处章昏镇棍弓瘪害幂褒沮筷帝悬怒烧锁社耙秩驻非动峦阔职体剐考股洒韦阀协尝娃泣脑狄千衡换黔星们峡仪"
            + "浙姻蜀砒措疑啃暑资被歧兜屯秃诸轴傣锑爆穗腻隘润央唱牙纷垫铀榷稀粒殷忆辩厄野蔷靴若氯定彻弟如恼几妓宅隆拈菠探染斑娜硝挥掉债年"
            + "屿绰铁虚烬坚耗序邱慕沁苯噬温诵屁咸设聂需要忱熟轩奠溅锄揖南串彩哭手碰蛙媳叹夕募傲遁苫蛹瞬蒂发纳贼嚷各治抬梁它倪绚剔斤耿戒露"
            + "舍孪庇讨氟哉卜派诚靶毁盆泉象拍匹砾炯媒撤饥全吊躁诬吐授榴呢蓄瞒箍臀永服瘟莉胞怔冀孜畜偿挑磕悄色缎盒玄痘珍贾项毫枪堤班凝诲陈"
            + "蹬并趣取恋鲍宴秧畴跨扣痞钞龋霍碗整柄负旭杨银卖鹿乙煮九讼善衬奥患渊认答默蔬仔嘶魔宪门捻至督赏潜浇汐峙讹崭锡陷摇误添掂兵氮省"
            + "锋兰企贴舟澎酷襄坪肝郊盾审茎惰炕掇狈笆桃提究子保格氨蹲恢篓分公奈际旷彼令竿玲窟薪谣墒展羚勃养步鞘花袜闪道徽即升砸祟荆脚瘸趁"
            + "敝悲存仑座婴嘘玛铬尖轨秸嘉鹏絮匀雏芥效是举埂惑溺酥想华康钦附尼吁捣漠邵伪婉咽矩斗挪昂灵疥汇田庞稽钎垮奢古闺容违师钓跪拥佩缉"
            + "见驱霹哥缘相仗汤形挂抠卿傅计诡糜涕书希司撕猪跋螟缄谆哀徒茸惫骆慰橡眶摘咏鼎渴奸厦权顺驮志拖裹胺味属屡拦燃碧蚌豪迂维睹撂邓颇"
            + "费淀笺眷敬瞩疡源讥隅氰和距帮冒袖诫壤画珐竹跺蠕抿酸元棘搅减客辰两柬涨款侠掷鼠侣届纬吱厕欠沉挖铅枚甜圆告驼癸危惦宣歪集亥氓吾"
            + "泪矣逢一霓擂矛寸妥闷弹那掐靠搞疾过涅中粳废窿践捅霸狰拣猜袒羹拂宠孕持明挝扩慨俊扭岩压矽痊划馒腑陋割敞篇境突酮坟凸姜食颊愁叼"
            + "储柴症凄前揩学孙玖沸绞收貉犯础辨肋卡岁捡崩魏袍忙卉绷爹键地填件掏醚弗币缓嫩蔡瘩禽杏赡衙窥平电莎图颤吕谍躯婶凿贬耘度挺妊僳莹"
            + "衷阿俞恒鸽漓艰僚誉刑虞臭代奔将扰辈呈邑翰左言丹凌品艳扛纽蝎鼓糟鹃渍庙迈材耸膨购喉钝碴药缨啸赚悔啄墅裤秋悸澄沿怂市猾螺英镊蟹"
            + "示逗殊壶点厘侯这逆嚏烩肠豌虫蛋接脉拱芬与愈壁依殆肾铭摧火荣忠六逐莱罪匡熬藏眠义宙无腰税诌檬航显涎跌赔涌阴敛务案摆吭阜篱簿懈"
            + "蠢纲湃握翱延毕尤茄览阳桌植柯燕淮碟薛愤瓶痉档称绵照丝辖报什伯琉札册聘苟乔蹄沧咀搀化拒雕哺眺匈腆廊诅音剁轰跟枷估实栗忍难揍山"
            + "蔗砧含州准肇任艘砷葡汰屹铜吠津藐懒涩包廷轿播恤芋畅竣琼峰倔遂匙扦城拯搐赂弃可贵缕霞周涡玉浑礁蜡巳太合哲堆弧恫郁壮观噎酉欢扒"
            + "羞瞪僵颂皖跑朔仙屎脆硫膜砰磐租驴急做贸酌熏踞鬼虾途锥澈岔狙乳单欲堡藤廉辱驹节隋馋淤灯踩泰成蝴顶吮母采柏咎责占页晨荡筛裳艇习"
            + "远魂掳趾妒淋孺晴赵讶溜妇鸣拇骄于厢纺寞宋冗竖甫毛滩烘倚廓截淆价很钠斋最毋锅标鲤捷初吞潘质寥覆疙膘宜克辫陛缩曰人府家瓢乃他美"
            + "像井汛版甲镶宾催帐尸累遥唬奶亦轻七嫁匠侮洼扳链颠靳脯昌官沾丙搂杜盛席消凛梯范颁籽病喊而辊抉坑燎粹账慎追均辜皆贝恿阻旨训柔赁"
            + "堕陇匆浮量喧钳苹蕉进窘舜恳种伍险革胎瞎肚昨棋晾扎煞铰诧汁破";
    
    private static final int MAX_NUMERIC_LETTER_CHAR = 256;
    
    @Getter
    private Properties props;
    
    private int delta;
    
    private int mask;
    
    private int start;
    
    private Map<Character, Integer> charIndexes;
    
    @Override
    public void init(final Properties props) {
        this.props = props;
        delta = createDelta(props);
        mask = createMask(props);
        start = createStart(props);
        charIndexes = createCharIndexes(props);
    }
    
    private int createDelta(final Properties props) {
        if (props.containsKey(DELTA)) {
            String delta = props.getProperty(DELTA);
            try {
                return Integer.parseInt(delta);
            } catch (NumberFormatException ex) {
                throw new EncryptAlgorithmInitializationException("CHAR_DIGEST_LIKE", "delta can only be a decimal number");
            }
        }
        return DEFAULT_DELTA;
    }
    
    private int createMask(final Properties props) {
        if (props.containsKey(MASK)) {
            String mask = props.getProperty(MASK);
            try {
                return Integer.parseInt(mask);
            } catch (NumberFormatException ex) {
                throw new EncryptAlgorithmInitializationException("CHAR_DIGEST_LIKE", "mask can only be a decimal number");
            }
        }
        return DEFAULT_MASK;
    }
    
    private int createStart(final Properties props) {
        if (props.containsKey(START)) {
            String start = props.getProperty(START);
            try {
                return Integer.parseInt(start);
            } catch (NumberFormatException ex) {
                throw new EncryptAlgorithmInitializationException("CHAR_DIGEST_LIKE", "start can only be a decimal number");
            }
        }
        return DEFAULT_START;
    }
    
    private Map<Character, Integer> createCharIndexes(final Properties props) {
        String dictContent = props.containsKey(DICT) && !Strings.isNullOrEmpty(props.getProperty(DICT)) ? props.getProperty(DICT) : DEFAULT_DICT;
        Map<Character, Integer> result = new HashMap<>(dictContent.length(), 1);
        for (int index = 0; index < dictContent.length(); index++) {
            result.put(dictContent.charAt(index), index);
        }
        return result;
    }
    
    @Override
    public String encrypt(final Object plainValue, final EncryptContext encryptContext) {
        return null == plainValue ? null : digest(String.valueOf(plainValue));
    }
    
    private String digest(final String plainValue) {
        StringBuilder result = new StringBuilder(plainValue.length());
        for (char each : plainValue.toCharArray()) {
            char maskedChar = getMaskedChar(each);
            if ('%' == maskedChar) {
                result.append(each);
            } else {
                result.append(maskedChar);
            }
        }
        return result.toString();
    }
    
    private char getMaskedChar(final char originalChar) {
        if ('%' == originalChar) {
            return originalChar;
        }
        if (originalChar <= MAX_NUMERIC_LETTER_CHAR) {
            return (char) ((originalChar + delta) & mask);
        }
        if (charIndexes.containsKey(originalChar)) {
            return (char) (((charIndexes.get(originalChar) + delta) & mask) + start);
        }
        return (char) (((originalChar + delta) & mask) + start);
    }
    
    @Override
    public String decrypt(final String cipherValue, final EncryptContext encryptContext) {
        return cipherValue;
    }
    
    @Override
    public String getType() {
        return "CHAR_DIGEST_LIKE";
    }
}
