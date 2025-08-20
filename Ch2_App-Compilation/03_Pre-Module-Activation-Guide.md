

## Module-and Device-Specific Performance Experiments

This document presents detailed, module-and device-specific performance measurements of the Screenomics platform. Our goal is to provide researchers with (rough) estimates of battery, RAM, CPU, and storage impacts for different modules and devices, enabling better planning of studies, particularly for participants using older or lower-end smartphones. These measurements are intended to help researchers anticipate system demands and potential constraints, especially in populations with varied access to high-performance devices. By sharing these results, we aim to support study planning and reproducibility while highlighting practical considerations for implementing digital phenotyping applications.

---

### Devices Tested

We selected three Android devices representing a spectrum of performance capabilities:

1. **BLU G53 (Android 13)** – an older, low-end device equipped with a Unisoc Tiger T310 processor, 4 GB RAM, 64 GB storage, and a 5000 mAh battery. Screen resolution is 1600×720.
2. **Samsung Galaxy S21 (Android 14)** – a mid-range device with an Exynos 2100 processor, 8 GB RAM, 256 GB storage, and a 4000 mAh battery. Screen resolution is 2400×1080.
3. **Google Pixel Fold (Android 15)** – a high-end device featuring a Tensor G5 processor, 12 GB RAM, 512 GB storage, and a 5000 mAh battery. Screen resolution is 2208×1840.

These devices were chosen to represent a range from low-end to high-end smartphones commonly found among study participants.

| Device       | OS Version | Processor       | RAM  | Storage | Battery Capacity | Resolution |
|-------------|------------|----------------|------|---------|-----------------|------------|
| BLU G53     | Android 13 | Unisoc Tiger T310 | 4 GB | 64 GB   | 5000 mAh        | 1600x720   |
| Samsung Galaxy S21 | Android 14 | Exynos 2100     | 8 GB | 256 GB  | 4000 mAh        | 2400x1080  |
| Google Pixel Fold | Android 15 | Tensor G5       | 12 GB | 512 GB | 5000 mAh        | 2208x1840  |


---

### Experimental Methodology

Each module was tested independently on all three devices during a 2-hour session. Modules were not combined to avoid additive effects, and resource usage reflects only the specific module being tested. Four key performance metrics were recorded: storage requirements (MB), RAM usage (MB), CPU load (average percent), and and battery consumption (percent drained per session). Note that RAM, CPU, and battery usage also inevitably reflect experiment-specific characteristics such as background processing, video decoding, and real-time data conversion.

---

### Modules Tested and Experiment Settings

1. **Screenshots Module**: For the Screenshots module, screenshots were captured every 5 seconds at the highest quality without compression. During the experiment, a 2-hour video was played. All screenshot images were saved locally.


2. **Apps Module**: The Apps module involved a background service that checked every second whether the currently active foreground app had changed. Only app switch events were logged, including the new foreground app package name and timestamp. To simulate typical smartphone usage behavior, Instagram and Facebook were alternated every 5 minutes over the 2-hour session.

3. **Interactions Module**: The Interactions module measured the resource usage of Instagram Reels scrolling for 2 hours, with touch interaction events logged every 10 seconds.

4. **Activities Module**: For the Activities module, step counts were recorded every 10 minutes while the devices were kept in pockets with screens off.
5. **Locations Module**: The Locations module involved GPS data collection every 10 minutes while the devices were kept in a vehicle during continuous driving with screens off.

---

### Performance Metrics

The following metrics were measured for each module:

- **Battery consumption (% per hour)**: This metric represents the proportion of the device’s battery drained per hour while running a specific module. It is important because high battery usage may reduce participant compliance, especially in studies where participants are expected to carry the device throughout the day. Understanding battery impact helps researchers anticipate constraints and optimize module settings.
- **RAM usage (MB)**: RAM usage indicates the amount of active memory the module consumes while running. High RAM usage can slow down the device or interfere with other apps, particularly on older or low-memory smartphones. Measuring RAM usage ensures that the platform can run smoothly without causing system instability.
- **CPU load (% average)**: CPU load represents the average processing power used by the module over time. High CPU usage can generate heat, reduce battery life, and affect the performance of other apps. Knowing CPU demands helps researchers understand the computational cost of each module and plan accordingly.
- **Storage requirements (MB)**: Storage requirements measure how much data the module generates and saves locally (or remotely) over time. This is critical for ensuring that devices have enough space to store collected data without disrupting normal usage or causing data loss.

---

### Results

| Module        | Device | Storage (MB) | Storage (%) | RAM Usage (MB) | CPU Load (%) | Battery Drain (%) |
|---------------|-------|--------------|------------|----------------|--------------|-----------------|
| Screenshots   | BLU   | 1,827        | 2.79%      | 330            | 24.8         | 20.5            |
|               | Galaxy| 3,742        | 1.43%      | 241            | 12.5         | 11.5            |
|               | Pixel | 5,123        | 0.98%      | 247            | 11.8         | 7.6             |
| Apps          | BLU   | 0.012        | 0.000018%  | 425            | 22.9         | 21.5            |
|               | Galaxy| 0.012        | 0.000005%  | 454            | 20.6         | 10.3            |
|               | Pixel | 0.012        | 0.000002%  | 473            | 18.4         | 9.2             |
| Interactions  | BLU   | 0.14         | 0.00021%   | 403            | 22.5         | 28.2            |
|               | Galaxy| 0.14         | 0.000053%  | 453            | 20.5         | 15.2            |
|               | Pixel | 0.14         | 0.000027%  | 473            | 18.5         | 12.0            |
| Activities    | BLU   | 0.0012       | 0.000002%  | 8              | 3.0          | 4.0             |
|               | Galaxy| 0.0012       | 0.0000005% | 6              | 2.0          | 2.5             |
|               | Pixel | 0.0012       | 0.0000002% | 5              | 1.5          | 1.5             |
| Locations     | BLU   | 0.014        | 0.000021%  | 14             | 6.9          | 13.5            |
|               | Galaxy| 0.014        | 0.0000053% | 11             | 4.5          | 10.0            |
|               | Pixel | 0.014        | 0.0000027% | 9              | 3.0          | 8.2             |


Screenshots were the most resource-intensive, especially on older devices due to limited RAM and processor capabilities. Background app monitoring and user interactions incurred moderate CPU and RAM usage, with battery drain proportional to the intensity of logging and foreground activity. Activities and GPS location tracking were relatively lightweight in RAM and storage but GPS sensing contributed to noticeable battery consumption. Across all modules, differences between devices were observable but manageable, and no device failed to perform reliably.




[Back to Top](#top)



