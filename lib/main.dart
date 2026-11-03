import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: ReporteStockScreen(),
  ));
}

class ReporteStockScreen extends StatefulWidget {
  const ReporteStockScreen({super.key});

  @override
  State<ReporteStockScreen> createState() => _ReporteStockScreenState();
}

class _ReporteStockScreenState extends State<ReporteStockScreen> {
  // Con 'adb reverse tcp:8080 tcp:8080', localhost apunta directo a la PC por el cable USB
  final String apiUrl = 'http://localhost:8080/api/repuestos-bajo-stock';

  Future<List<dynamic>> fetchRepuestos() async {
    final response = await http.get(Uri.parse(apiUrl));

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Error del servidor: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Reporte: Repuestos Bajo Stock'),
        backgroundColor: Colors.indigo,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () => setState(() {}),
          ),
        ],
      ),
      body: FutureBuilder<List<dynamic>>(
        future: fetchRepuestos(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Text(
                  'Error de conexión:\n${snapshot.error}',
                  textAlign: TextAlign.center,
                  style: const TextStyle(color: Colors.red, fontSize: 16),
                ),
              ),
            );
          }

          final repuestos = snapshot.data ?? [];

          if (repuestos.isEmpty) {
            return const Center(
              child: Text(
                'No hay repuestos con stock crítico (≤ 5)',
                style: TextStyle(fontSize: 16, color: Colors.grey),
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: repuestos.length,
            itemBuilder: (context, index) {
              final item = repuestos[index];
              return Card(
                elevation: 2,
                margin: const EdgeInsets.symmetric(vertical: 6),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                child: ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.red.shade100,
                    child: Text(
                      '${item['existencia'] ?? 0}',
                      style: const TextStyle(
                        color: Colors.red,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  title: Text(
                    item['nombre'] ?? 'Sin nombre',
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: Text('Código / ID: ${item['id']}'),
                  trailing: Text(
                    'Q ${item['precio'] ?? 0.0}',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                      color: Colors.green,
                    ),
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}