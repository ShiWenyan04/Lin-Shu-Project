package com.ruoyi.common.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KML解析工具类 (终极兼容版)
 */
public class KmlUtils {

    public static Map<String, Object> parseKml(MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> points = new ArrayList<>();
        // 使用 List<List<Double[]>> 结构来存储多边形路径，适配高德 Polygon path
        List<List<Double[]>> boundaries = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        // 1. 递归查找所有的 Placemark
        List<Element> placemarks = new ArrayList<>();
        recursiveFind(root, "Placemark", placemarks);

        for (Element pm : placemarks) {
            // === 解析点位 (Point) ===
            parsePoint(pm, points);

            // === 解析多边形 (Polygon / MultiGeometry) ===
            parseBoundary(pm, boundaries);
        }

        result.put("points", points);
        // 转换成 JSON 字符串: [[[lng,lat],...], [[lng,lat],...]]
        result.put("boundary", boundaries.isEmpty() ? null : toJsonString(boundaries));
        return result;
    }

    // 递归查找指定标签
    private static void recursiveFind(Element element, String targetName, List<Element> result) {
        if (targetName.equals(element.getName())) {
            result.add(element);
        }
        for (Object child : element.elements()) {
            recursiveFind((Element) child, targetName, result);
        }
    }

    // 解析点位逻辑
    private static void parsePoint(Element pm, List<Map<String, Object>> points) {
        List<Element> pointEles = new ArrayList<>();
        recursiveFind(pm, "Point", pointEles);

        for (Element pointEle : pointEles) {
            Element coordEle = pointEle.element("coordinates");
            if (coordEle != null) {
                String[] parts = coordEle.getTextTrim().split(",");
                if (parts.length >= 2) {
                    Map<String, Object> map = new HashMap<>();
                    Element nameEle = pm.element("name");
                    String name = (nameEle != null) ? nameEle.getTextTrim() : "未命名";
                    map.put("siteCodeRaw", name);
                    map.put("siteCode", name.split("-")[0]);
                    map.put("longitude", new BigDecimal(parts[0].trim()));
                    map.put("latitude", new BigDecimal(parts[1].trim()));
                    points.add(map);
                }
            }
        }
    }

    // 解析边界逻辑 (支持 MultiGeometry 嵌套)
    private static void parseBoundary(Element pm, List<List<Double[]>> boundaries) {
        // 查找该 Placemark 下所有的 Polygon
        List<Element> polygons = new ArrayList<>();
        recursiveFind(pm, "Polygon", polygons);

        for (Element poly : polygons) {
            Element outer = poly.element("outerBoundaryIs");
            if (outer != null) {
                Element ring = outer.element("LinearRing");
                if (ring != null) {
                    Element coords = ring.element("coordinates");
                    if (coords != null) {
                        List<Double[]> path = parseCoordsToPath(coords.getTextTrim());
                        if (!path.isEmpty()) {
                            boundaries.add(path);
                        }
                    }
                }
            }
        }
    }

    // 坐标字符串清洗： "108.1,28.1,0 108.2,28.2,0" -> List<[108.1, 28.1]>
    private static List<Double[]> parseCoordsToPath(String raw) {
        List<Double[]> path = new ArrayList<>();
        String[] pairs = raw.trim().split("\\s+"); // 按空格分割
        for (String pair : pairs) {
            String[] lngLat = pair.split(",");
            if (lngLat.length >= 2) {
                try {
                    Double lng = Double.parseDouble(lngLat[0].trim());
                    Double lat = Double.parseDouble(lngLat[1].trim());
                    path.add(new Double[]{lng, lat});
                    // 忽略第3位的高程
                } catch (NumberFormatException e) {
                    // 忽略解析错误的坐标
                }
            }
        }
        return path;
    }

    // 手动拼 JSON，防止引入 Jackson 依赖报错
    private static String toJsonString(List<List<Double[]>> boundaries) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < boundaries.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("[");
            List<Double[]> path = boundaries.get(i);
            for (int j = 0; j < path.size(); j++) {
                if (j > 0) sb.append(",");
                sb.append("[").append(path.get(j)[0]).append(",").append(path.get(j)[1]).append("]");
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }
}