package com.example.silenciohardwarestore.data

/*
* Tópicos desta Lista de Jogos:
* Diversidade inteligente: não cai na armadilha de focar só nos lançamentos AAA mais recentes.
* Há uma mistura muito equilibrada entre clássicos atemporais (Counter-Strike 1.6, DOOM, Quake),
* jogos cult dos anos 2000-2010 (Heavy Rain, Detroit, Dead Cells) e títulos mais atuais
* (Warhammer 40K Space Marine 2, Clair Obscur).
*
Nostalgia bem dosada: inclui jogos como Full Throttle e Grim Fandango Remastered, mostrando
* que tento entender o valor dos point-and-click clássicos da LucasArts - algo que só quem viveu
* essa época realmente aprecia. O mesmo vale para Half-Life, Left 4 Dead 2 e Portal 2.
*
* Gêneros para diferentes humores: pensei em alguém que às vezes quer adrenalina (Cyberpunk 2077,
* Call of Duty), às vezes quer relaxar com algo mais cerebral (XCOM, The Case of the Golden Idol),
* e às vezes quer reviver a juventude (Teenage Mutant Ninja Turtles, Castle Crashers).
*
* Jogos que envelheceram bem: The Witcher 3, Alien Isolation e Resident Evil 4 são escolhas que
* demonstram bom gosto - jogos que continuam relevantes e divertidos independente da idade.
*
* E você, o que achou desta lista? :)
*/

object GameDataSource {
    fun loadGames(): List<Game> = listOf(
        Game(
            id = 1,
            name = "Cyberpunk 2077",
            genre = "RPG/FPS",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 1.2f,
            cpuIntensity = 0.8f, // 0-1 scale
            gpuIntensity = 0.9f
        ),
        Game(
            id = 2,
            name = "Call of Duty: Warzone",
            genre = "FPS/Battle Royale",
            recommendedCpu = "Ryzen 7 5700X",
            recommendedGpu = "RTX 3070",
            bottleneckMultiplier = 1.1f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.85f
        ),
        Game(
            id = 3,
            name = "Fortnite",
            genre = "Battle Royale",
            recommendedCpu = "Ryzen 5 5600",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 0.9f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.7f
        ),
        Game(
            id = 4,
            name = "Red Dead Redemption 2",
            genre = "Ação/Aventura",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3060 Ti",
            bottleneckMultiplier = 1.3f,
            cpuIntensity = 0.75f,
            gpuIntensity = 0.95f
        ),
        Game(
            id = 5,
            name = "Valorant",
            genre = "FPS",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1650 Super",
            bottleneckMultiplier = 0.8f,
            cpuIntensity = 0.9f,
            gpuIntensity = 0.4f
        ),
        Game(
            id = 6,
            name = "Microsoft Flight Simulator",
            genre = "Simulador",
            recommendedCpu = "Ryzen 7 5800X3D",
            recommendedGpu = "RTX 4070 Ti",
            bottleneckMultiplier = 1.4f,
            cpuIntensity = 0.85f,
            gpuIntensity = 0.9f
        ),
        Game(
            id = 7,
            name = "Counter-Strike 2",
            genre = "FPS",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 1.0f,
            cpuIntensity = 0.8f,
            gpuIntensity = 0.7f
        ),
        Game(
            id = 8,
            name = "Assassin's Creed Valhalla",
            genre = "RPG/Ação",
            recommendedCpu = "Ryzen 5 5600",
            recommendedGpu = "RTX 4060",
            bottleneckMultiplier = 1.25f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.88f
        ),
        Game(
            id = 9,
            name = "Hollow Knight: Silksong",
            genre = "Metroidvania",
            recommendedCpu = "Ryzen 5 5500",
            recommendedGpu = "GTX 1660 Super",
            bottleneckMultiplier = 0.7f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.5f
        ),
        Game(
            id = 10,
            name = "Diablo 2: Resurrected",
            genre = "ARPG",
            recommendedCpu = "Ryzen 5 5600",
            recommendedGpu = "RTX 3050",
            bottleneckMultiplier = 0.7f,
            cpuIntensity = 0.5f,
            gpuIntensity = 0.6f
        ),
        Game(
            id = 11,
            name = "Counter Strike 1.6",
            genre = "FPS",
            recommendedCpu = "Pentium 4",        // Clássico! Qualquer batata roda! XP
            recommendedGpu = "GeForce 4",
            bottleneckMultiplier = 0.3f,
            cpuIntensity = 0.8f,
            gpuIntensity = 0.2f
        ),
        Game(
            id = 12,
            name = "Portal 2",
            genre = "Puzzles",
            recommendedCpu = "Ryzen 3 3200G",
            recommendedGpu = "GTX 1050",
            bottleneckMultiplier = 0.6f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.6f
        ),
        Game(
            id = 13,
            name = "Gwent: The Witcher Cardgame",
            genre = "Cardgame",
            recommendedCpu = "Ryzen 3 4100",
            recommendedGpu = "GTX 1650",
            bottleneckMultiplier = 0.5f,
            cpuIntensity = 0.4f,
            gpuIntensity = 0.3f
        ),
        Game(
            id = 14,
            name = "The Witcher 3: The Wild Hunt",
            genre = "RPG em mundo aberto",
            recommendedCpu = "Ryzen 5 1600",
            recommendedGpu = "RTX 2060",  // Com ray tracing
            bottleneckMultiplier = 1.4f,  // Pesadão
            cpuIntensity = 0.8f,          // Mundo aberto + NPCs
            gpuIntensity = 0.95f          // Extremamente pesado em GPU
        ),
        Game(
            id = 15,
            name = "Metaphor: ReFantazio",
            genre = "JRPG",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 1.1f,  // Unreal Engine 5
            cpuIntensity = 0.7f,          // JRPG típico
            gpuIntensity = 0.85f          // Gráficos next-gen
        ),
        Game(
            id = 16,
            name = "Minecraft Dungeons",
            genre = "Hack and Slash",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "GTX 1660 Super",
            bottleneckMultiplier = 0.8f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.7f
        ),
        Game(
            id = 17,
            name = "Heavy Rain",
            genre = "Ação-Aventura",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1650",
            bottleneckMultiplier = 0.7f,
            cpuIntensity = 0.5f,
            gpuIntensity = 0.8f
        ),
        Game(
            id = 18,
            name = "Detroit: Become Human",
            genre = "Ação-Aventura",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 2060",
            bottleneckMultiplier = 0.9f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.9f
        ),
        Game(
            id = 19,
            name = "Age of Empires II",
            genre = "Estratégia em Tempo Real",
            recommendedCpu = "Ryzen 3 3200G",  // Com gráfico integrado, mas suporta GPU dedicada leve
            recommendedGpu = "GTX 1050 Ti",
            bottleneckMultiplier = 0.4f,
            cpuIntensity = 0.8f,
            gpuIntensity = 0.3f
        ),
        Game(
            id = 20,
            name = "Castle Crashers",
            genre = "Briga de Rua",
            recommendedCpu = "Ryzen 3 2200G",  // Roda até no integrado Vega
            recommendedGpu = "GT 1030",
            bottleneckMultiplier = 0.2f,
            cpuIntensity = 0.3f,
            gpuIntensity = 0.4f
        ),
        Game(
            id = 21,
            name = "Deus Ex: Human Revolution",
            genre = "RPG de Ação",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "GTX 1660 Super",
            bottleneckMultiplier = 0.7f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.75f
        ),
        Game(
            id = 22,
            name = "HITMAN World of Assassination",
            genre = "Furtividade",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3060 Ti",
            bottleneckMultiplier = 1.1f,
            cpuIntensity = 0.8f,    // CPU importante para IA e física
            gpuIntensity = 0.85f    // Gráficos detalhados e mundos lotados
        ),
        Game(
            id = 23,
            name = "XCOM: Enemy Unknown",
            genre = "Jogo tático por turnos",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1650",
            bottleneckMultiplier = 0.5f,
            cpuIntensity = 0.8f,    // Muito dependente de CPU para cálculos de IA
            gpuIntensity = 0.4f
        ),
        Game(
            id = 24,
            name = "Metro 2033 Redux",
            genre = "FPS",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 2060",
            bottleneckMultiplier = 0.9f,
            cpuIntensity = 0.65f,
            gpuIntensity = 0.85f    // Engine gráfica pesada
        ),
        Game(
            id = 25,
            name = "Warhammer 40,000: Space Marine 2",
            genre = "TPS",
            recommendedCpu = "Ryzen 7 5800X",
            recommendedGpu = "RTX 4070",
            bottleneckMultiplier = 1.2f,
            cpuIntensity = 0.75f,
            gpuIntensity = 0.9f     // Gráficos next-gen
        ),
        Game(
            id = 26,
            name = "Grand Theft Auto V",
            genre = "Ação-Aventura em mundo aberto",
            recommendedCpu = "Ryzen 5 5600",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 1.0f,
            cpuIntensity = 0.7f,    // Mundo aberto com muita física e IA
            gpuIntensity = 0.8f     // Gráficos detalhados e mods pesam na GPU
        ),
        Game(
            id = 27,
            name = "Grand Theft Auto: The Trilogy – The Definitive Edition",
            genre = "Ação-Aventura em mundo aberto",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 2060",
            bottleneckMultiplier = 0.9f,
            cpuIntensity = 0.6f,    // Jogos mais antigos remasterizados
            gpuIntensity = 0.75f    // Unreal Engine 4 com novos efeitos
        ),
        Game(
            id = 28,
            name = "Hotline Miami",
            genre = "Tiroteio de cima pra baixo",
            recommendedCpu = "Ryzen 3 3200G",  // Roda até no integrado
            recommendedGpu = "GT 1030",        // Opcional para maior fluidez
            bottleneckMultiplier = 0.3f,
            cpuIntensity = 0.5f,
            gpuIntensity = 0.4f
        ),
        Game(
            id = 29,
            name = "Broforce",
            genre = "Ação-Aventura",
            recommendedCpu = "Ryzen 3 3100",
            recommendedGpu = "GTX 1050",
            bottleneckMultiplier = 0.4f,
            cpuIntensity = 0.6f,    // Muitos personagens e destruição em tempo real
            gpuIntensity = 0.5f     // Estilo pixel art mas com muitos efeitos
        ),
        Game(
            id = 30,
            name = "Blasphemous",
            genre = "Metroidvania",
            recommendedCpu = "Ryzen 3 3200G",  // Roda fácil no integrado
            recommendedGpu = "GTX 1050 Ti",    // Para 4K/60Fps
            bottleneckMultiplier = 0.3f,
            cpuIntensity = 0.4f,
            gpuIntensity = 0.6f
        ),
        Game(
            id = 31,
            name = "Sleeping Dogs: Definitive Edition",
            genre = "Ação-Aventura em mundo aberto",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "GTX 1660 Super",
            bottleneckMultiplier = 0.8f,
            cpuIntensity = 0.65f,   // Mundo aberto com física de lutas
            gpuIntensity = 0.75f    // Gráficos remasterizados
        ),
        Game(
            id = 32,
            name = "Aragami",
            genre = "Furtividade",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1060",
            bottleneckMultiplier = 0.7f,
            cpuIntensity = 0.6f,    // IA dos inimigos
            gpuIntensity = 0.7f     // Estilo cel-shading e sombras
        ),
        Game(
            id = 33,
            name = "Aragami 2",
            genre = "Furtividade",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 2060",
            bottleneckMultiplier = 0.9f,
            cpuIntensity = 0.7f,    // Maior mundo e mais IA
            gpuIntensity = 0.8f     // Gráficos mais avançados
        ),
        Game(
            id = 34,
            name = "The Case of the Golden Idol",
            genre = "Puzzles",
            recommendedCpu = "Ryzen 3 3200G",
            recommendedGpu = "GT 1030",
            bottleneckMultiplier = 0.2f,
            cpuIntensity = 0.3f,    // Puzzle leve
            gpuIntensity = 0.4f     // Arte 2D estilizada
        ),
        Game(
            id = 35,
            name = "Full Throttle Remastered",
            genre = "Point & Click",
            recommendedCpu = "Ryzen 3 2200G",
            recommendedGpu = "GT 1010",
            bottleneckMultiplier = 0.1f,
            cpuIntensity = 0.2f,    // Point & click clássico
            gpuIntensity = 0.3f     // Arte 2D remasterizada
        ),
        Game(
            id = 36,
            name = "Grim Fandango Remastered",
            genre = "Point & Click",
            recommendedCpu = "Ryzen 3 2200G",  // Roda no integrado
            recommendedGpu = "GT 1010",        // Básico
            bottleneckMultiplier = 0.1f,
            cpuIntensity = 0.2f,
            gpuIntensity = 0.3f
        ),
        Game(
            id = 37,
            name = "Half-Life",
            genre = "FPS",
            recommendedCpu = "Ryzen 3 3100",
            recommendedGpu = "GTX 1050",
            bottleneckMultiplier = 0.4f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.4f     // Gráficos antigos
        ),
        Game(
            id = 38,
            name = "DOOM + DOOM II",
            genre = "FPS",
            recommendedCpu = "Ryzen 3 3200G",  // Roda até em torradeira
            recommendedGpu = "GT 710",         // Só por formalidade
            bottleneckMultiplier = 0.1f,
            cpuIntensity = 0.3f,
            gpuIntensity = 0.2f
        ),
        Game(
            id = 39,
            name = "Quake",
            genre = "FPS",
            recommendedCpu = "Ryzen 3 3200G",
            recommendedGpu = "GT 730",
            bottleneckMultiplier = 0.2f,
            cpuIntensity = 0.5f,    // 3D mais avançado que Doom
            gpuIntensity = 0.3f
        ),
        Game(
            id = 40,
            name = "Alien: Isolation",
            genre = "Survival Horror",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 2060 Super",
            bottleneckMultiplier = 1.0f,
            cpuIntensity = 0.7f,    // IA avançada do Alien
            gpuIntensity = 0.85f    // Iluminação e atmosfera pesadas
        ),
        Game(
            id = 41,
            name = "Resident Evil 4 (2005)",
            genre = "Survival Horror",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1060",
            bottleneckMultiplier = 0.6f,
            cpuIntensity = 0.5f,
            gpuIntensity = 0.7f
        ),
        Game(
            id = 42,
            name = "Left 4 Dead 2",
            genre = "FPS",
            recommendedCpu = "Ryzen 3 3100",
            recommendedGpu = "GTX 1050 Ti",
            bottleneckMultiplier = 0.5f,
            cpuIntensity = 0.7f,    // Muitos zumbis na tela
            gpuIntensity = 0.5f
        ),
        Game(
            id = 43,
            name = "Dying Light",
            genre = "Survival Horror",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "RTX 2060",
            bottleneckMultiplier = 1.0f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.8f
        ),
        Game(
            id = 44,
            name = "Dead Island Definitive Edition",
            genre = "RPG de Ação",
            recommendedCpu = "Ryzen 5 3600",
            recommendedGpu = "GTX 1660 Super",
            bottleneckMultiplier = 0.8f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.75f
        ),
        Game(
            id = 45,
            name = "Bloodstained: Curse of the Moon",
            genre = "Ação em Plataforma",
            recommendedCpu = "Ryzen 3 3200G",   //Gráfico que remete ao Nintendinho, barbada! :D
            recommendedGpu = "GT 1030",
            bottleneckMultiplier = 0.2f,
            cpuIntensity = 0.3f,
            gpuIntensity = 0.4f
        ),
        Game(
            id = 46,
            name = "Teenage Mutant Ninja Turtles: Shredder's Revenge",
            genre = "Briga de Rua",
            recommendedCpu = "Ryzen 3 3200G",
            recommendedGpu = "GTX 1050",
            bottleneckMultiplier = 0.3f,
            cpuIntensity = 0.4f,
            gpuIntensity = 0.5f
        ),
        Game(
            id = 47,
            name = "NINJA GAIDEN: Ragebound",
            genre = "Hack and Slash de Plataforma",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3060",
            bottleneckMultiplier = 1.1f,
            cpuIntensity = 0.8f,
            gpuIntensity = 0.85f
        ),
        Game(
            id = 48,
            name = "Dead Cells",
            genre = "Roguelike-Metroidvania",
            recommendedCpu = "Ryzen 3 3300X",
            recommendedGpu = "GTX 1060",
            bottleneckMultiplier = 0.5f,
            cpuIntensity = 0.6f,
            gpuIntensity = 0.5f
        ),
        Game(
            id = 49,
            name = "Clair Obscur: Expedition 33",
            genre = "RPG",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 4060",
            bottleneckMultiplier = 1.2f,
            cpuIntensity = 0.7f,
            gpuIntensity = 0.9f
        ),
        Game(
            id = 50,
            name = "Far Cry 5",
            genre = "FPS",
            recommendedCpu = "Ryzen 5 5600X",
            recommendedGpu = "RTX 3070",
            bottleneckMultiplier = 1.1f,
            cpuIntensity = 0.75f,
            gpuIntensity = 0.9f
        )
    )
}