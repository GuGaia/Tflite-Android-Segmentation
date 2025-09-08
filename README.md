# TFLite Image Segmentation (Android)

## Visão geral
Aplicativo Android que **segmenta** objetos (gato/cão) em imagens escolhidas pelo usuário (câmera ou galeria) usando um modelo **TensorFlow Lite** (UNet, FP32).  
A inferência ocorre localmente no dispositivo e o app exibe **a imagem original** e **a máscara de segmentação sobreposta**.

O modelo foi treinado no dataset **Oxford-IIIT Pet** (PyTorch) e convertido para **TFLite FP32**.

---

## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/aa98b057-a2ab-416a-88cb-c9142fc988e0" alt="Screenshot_20250908_200703" width="24%" />
  <img src="https://github.com/user-attachments/assets/8256c2f5-cbef-4b39-b8f2-9a91436a6d23" alt="Screenshot_20250908_200855" width="24%" />
  <img src="https://github.com/user-attachments/assets/ec24ab18-e945-4865-afa8-0e1bdf2a3304" alt="Screenshot_20250908_200914" width="24%" />
  <img src="https://github.com/user-attachments/assets/2dc78dbf-9cb5-40f0-82f2-be44f352abcc" alt="Screenshot_20250908_200925" width="24%" />
</p>

---

## Recursos
- Seleção de imagem por **Câmera** ou **Galeria**
- **Inferência FP32** (CPU)
- **Overlay** da máscara (vermelho, `alpha=160`) sobre a imagem original
- **Threshold** padrão: **0.5**

---

## Arquitetura do projeto
- `app/src/main/java/com/.../MainActivity.kt` — UI/fluxo (seleção de imagem, botão **Predict**)
- `app/src/main/java/com/.../SegmentationInterpreter.kt` — wrapper do **TFLite Interpreter**
- `app/src/main/res/layout/activity_main.xml` — `ImageView` de entrada, `ImageView` do overlay, botões
- `app/src/main/assets/unet_pet_fp32.tflite` — **modelo TFLite FP32**

---

## Pré-requisitos
- **Android Studio** (com **JDK 17** configurado no Gradle)
- **Android SDK**: `compileSdk = 36` (ou ajuste para 35)
- Dispositivo físico ou emulador (**API 24+**)

---

## Como rodar

### 1) Abrir o projeto
Abra o repositório no Android Studio.

### 2) Sincronizar e compilar
- `File → Sync Project with Gradle Files`

### 3) Executar
1. Selecione um dispositivo (emulador ou físico)  
2. Clique em **Run ▶** para instalar e abrir o app  
3. Use **Load Photo** ou **Take Photo** e, em seguida, **Predict**

---

## Licença & créditos
- Modelo e app base inspirados nos **TensorFlow Lite Examples** e na aplicação:  
  https://github.com/Aandre99/Tflite-Android-Classification/tree/master
- Dataset: **Oxford-IIIT Pet**
