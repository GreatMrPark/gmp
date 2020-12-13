<?
define('NUMBER_OF_PARTICIPANTS', 16);

$participants = range(1,NUMBER_OF_PARTICIPANTS);
$bracket = getBracket($participants);
var_dump($bracket);

function getBracket($participants)
{
    $participantsCount = count($participants);  
    $rounds = ceil(log($participantsCount)/log(2));
    $bracketSize = pow(2, $rounds);
    $requiredByes = $bracketSize - $participantsCount;

    echo sprintf('Number of participants: %d<br/>%s', $participantsCount, PHP_EOL);
    echo sprintf('Number of rounds: %d<br/>%s', $rounds, PHP_EOL);
    echo sprintf('Bracket size: %d<br/>%s', $bracketSize, PHP_EOL);
    echo sprintf('Required number of byes: %d<br/>%s', $requiredByes, PHP_EOL);    

    if($participantsCount < 2)
    {
        return array();
    }

    $matches = array(array(1,2));

    for($round=1; $round < $rounds; $round++)
    {
        $roundMatches = array();
        $sum = pow(2, $round + 1) + 1;
        foreach($matches as $match)
        {
            $home = changeIntoBye($match[0], $participantsCount);
            $away = changeIntoBye($sum - $match[0], $participantsCount);
            $roundMatches[] = array($home, $away);
            $home = changeIntoBye($sum - $match[1], $participantsCount);
            $away = changeIntoBye($match[1], $participantsCount);
            $roundMatches[] = array($home, $away);
        }
        $matches = $roundMatches;
    }

    return $matches;

}

function changeIntoBye($seed, $participantsCount)
{
    //return $seed <= $participantsCount ?  $seed : sprintf('%d (= bye)', $seed);  
    return $seed <= $participantsCount ?  $seed : null;
}
?>