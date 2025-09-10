**🛠️ SILENCIO HARDWARE STORE

O APP DOS COMPARATIVOS DE HARDWARE QUE VAI TURBINAR SEU PC!

(Porque montar PC é arte, e arte a gente estuda! 👊🔥)
-------------------------------------------------------------------------------------------
🎯 FUNCIONALIDADES

⚡ COMPARAÇÃO DE CPUs

Analise e compare processadores lado a lado com detalhes técnicos completos.

    Mais de 90 CPUs cadastradas (Intel e AMD)

    Especificações detalhadas: clock, núcleos, TDP, temperaturas

    Modo grade e lista para visualização
    
-------------------------------------------------------------------------------------------
🎨 COMPARAÇÃO DE GPUs

Compare placas de vídeo e veja qual se destaca em desempenho e resfriamento.

    Mais de 80 GPUs cadastradas (NVIDIA, AMD e Intel)

    Dados completos: VRAM, clock, TDP, cooling recomendado

    Alertas de superaquecimento e problemas conhecidos
    
-------------------------------------------------------------------------------------------
🎮 DESEMPENHO EM JOGOS

Calcule bottleneck e veja como seu hardware se sai em jogos específicos.

    Seleção de CPU, GPU, jogo e resolução

    Análise inteligente de adequação do hardware

    Recomendações de configurações gráficas

    Previsão de performance com nível de confiança
    
-------------------------------------------------------------------------------------------
📊 ANÁLISE DETALHADA

Tabelas comparativas completas com diferenças percentuais e métricas técnicas.

🛠️ TECNOLOGIAS

Componente	          Tecnologia Usada

Linguagem	            Kotlin + Jetpack Compose

UI	                  100% Compose (Material 3)

Navegação	            Gerenciamento próprio de telas

Arquitetura	          MVVM com estado por composable

-------------------------------------------------------------------------------------------
📂 ESTRUTURA DO PROJETO
text

SilencioHardwareStore/

├── app/

│   ├── src/main/java/com/example/silenciohardwarestore/

│   │   ├── ui/components/      # Telas e componentes

│   │   │   ├── MainMenuScreen.kt

│   │   │   ├── CpuComparisonScreen.kt

│   │   │   ├── GpuComparisonScreen.kt

│   │   │   └── GamePerformanceScreen.kt

│   │   ├── data/              # Fontes de dados

|   |   |   |__ Cpu

|   |   |   |__ Gpu

|   |   |   |__ Game

│   │   │   ├── CpuDataSource.kt

│   │   │   ├── GpuDataSource.kt

│   │   │   └── GameDataSource.kt

│   │   ├── utils/             # Lógica de benchmark

│   │   │   ├── BenchmarkUtils.kt

│   │   │   ├── BenchmarkGpus.kt

│   │   │   └── BenchmarkGames.kt

│   │   ├── MainActivity.kt  

│   │   └── theme/             # Tema escuro profissional

├── build.gradle.kts           # Dependências principais

-------------------------------------------------------------------------------------------
📜 DEPENDÊNCIAS (já configuradas no projeto)
kotlin

// Jetpack Compose  
implementation(libs.androidx.activity.compose)  
implementation(platform(libs.androidx.compose.bom))  
implementation(libs.androidx.material3)

// AndroidX Core
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)

// Ícones extendidos
implementation("androidx.compose.material:material-icons-extended")

-------------------------------------------------------------------------------------------
🔧 VERSÕES CRÍTICAS

    Gradle: 8.12

    JDK: 11 (Requisito do Kotlin)

    Android Studio: Electric Eel ou superior

    Min SDK: 24 (Android 7.0)

    Target SDK: 36 (Android 14)
    
-------------------------------------------------------------------------------------------
💻 COMO RODAR

Terminal (Linux/macOS):
bash

git clone https://github.com/seu-usuario/SilencioHardwareStore.git
cd SilencioHardwareStore
./gradlew assembleDebug

-------------------------------------------------------------------------------------------
PowerShell (Windows):
bash

git clone https://github.com/seu-usuario/SilencioHardwareStore.git
cd SilencioHardwareStore
.\gradlew.bat assembleDebug

-------------------------------------------------------------------------------------------
Android Studio:

    Abra o projeto

    Build > Make Project

    Rode no emulador (Recomendado: Pixel 5 com API 34+)
    
-------------------------------------------------------------------------------------------
🎨 PREVIEW

Interface profissional com tema escuro, cards detalhados e visualização responsiva

-------------------------------------------------------------------------------------------
🚀 ROADMAP

Feature	Status

✅ Comparação CPU/GPU	Implementado

✅ Análise de bottleneck	Implementado

🔄 Exportar comparações	Planejado

🔄 Comunidade e reviews	Planejado

--------------------------------------------------------------------------------------------
📌 LICENÇA

Open-source sob GNU GPL v3.0 - use, modifique e contribua, mas mantenha o código livre para todos! 

Derivações devem usar a mesma licença.

-------------------------------------------------------------------------------------------
Feito com ódio de thermal throttle e paixão por boa performance em Rondonópolis/MT! ☕😎📱
